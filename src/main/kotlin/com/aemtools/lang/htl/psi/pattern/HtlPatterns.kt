package com.aemtools.lang.htl.psi.pattern

import com.aemtools.completion.util.findParentByType
import com.aemtools.completion.util.isInsideOf
import com.aemtools.constant.const.htl.DATA_SLY_CALL
import com.aemtools.constant.const.htl.DATA_SLY_INCLUDE
import com.aemtools.constant.const.htl.DATA_SLY_LIST
import com.aemtools.constant.const.htl.DATA_SLY_REPEAT
import com.aemtools.constant.const.htl.DATA_SLY_RESOURCE
import com.aemtools.constant.const.htl.DATA_SLY_TEMPLATE
import com.aemtools.constant.const.htl.DATA_SLY_TEST
import com.aemtools.constant.const.htl.DATA_SLY_USE
import com.aemtools.constant.const.htl.HTL_ATTRIBUTES
import com.aemtools.lang.htl.psi.HtlExpression
import com.aemtools.lang.htl.psi.HtlHtlEl
import com.aemtools.lang.htl.psi.HtlTypes.*
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns.*
import com.intellij.patterns.XmlPatterns.xmlAttribute
import com.intellij.patterns.XmlPatterns.xmlAttributeValue
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.xml.XmlTokenType.XML_NAME
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro Troynikov
 */
object HtlPatterns {

    /**
     * Matches the following:
     *
     * ```
     *    ${@ <caret>}
     * ```
     */
    val optionName: ElementPattern<PsiElement> =
            or(
                    psiElement()
                            .withParent(psiElement(VARIABLE_NAME)
                                    .withParent(psiElement(CONTEXT_EXPRESSION))),
                    psiElement()
                            .withParent(psiElement(VARIABLE_NAME)
                                    .withParent(psiElement(ASSIGNMENT)
                                            .withParent(psiElement(CONTEXT_EXPRESSION))))
            )

    /**
     * Matches option inside of data-sly-call, e.g.:
     *
     * ```
     *  <div data-sly-call="${@ <caret>}"
     * ```
     */
    val dataSlyCallOption: ElementPattern<PsiElement> =
            and(
                    optionName,
                    psiElement()
                            .inside(psiElement()
                                    .with(HtlTemplatePattern(DATA_SLY_CALL)))
            )

    /**
     * Matches option inside of data-sly-template, e.g.:
     *
     * ```
     *  <div data-sly-template="${@ <caret>}"
     * ```
     */
    val dataSlyTemplateOption: ElementPattern<PsiElement> =
            and(
                    optionName,
                    psiElement()
                            .inside(psiElement()
                                    .with(HtlTemplatePattern(DATA_SLY_TEMPLATE)))
            )

    /**
     * Matches option inside of data-sly-resource, e.g.:
     *
     * ```
     *  <div data-sly-resource="${@ <caret>}"
     * ```
     */
    val dataSlyResourceOption: ElementPattern<PsiElement> =
            and(
                    optionName,
                    psiElement()
                            .inside(psiElement()
                                    .with(HtlTemplatePattern(DATA_SLY_RESOURCE)))
            )

    /**
     * Matches the following:
     *
     * ```
     *    ${<caret>}
     *    ${@ option=<caret>
     *    ${ [<caret>, ...] }
     *    ${variable[<caret>]}
     * ```
     */
    val variableName: ElementPattern<PsiElement> =
            or<PsiElement>(
                    psiElement(VAR_NAME)
                            .andNot(psiElement().inside(psiElement(ACCESS_IDENTIFIER)))
                            .andNot(optionName),
                    psiElement(VAR_NAME)
                            .inside(psiElement(ARRAY_LIKE_ACCESS))
                            .andNot(optionName)
            )

    /**
     * Matches the following:
     *
     * ```
     *    ${'<caret'}
     *    ${"<caret>"}
     *    ${@ option='<caret>'}
     * ```
     */
    val stringLiteralValue: ElementPattern<PsiElement> =
            psiElement().inside(psiElement(STRING_LITERAL))

    /**
     * Matches the following:
     *
     * ```
     *    ${@ context='<caret>'}
     * ```
     */
    val contextOptionAssignment: ElementPattern<PsiElement> =
            and(
                    stringLiteralValue,
                    psiElement().inside(psiElement(CONTEXT_EXPRESSION)),
                    psiElement().inside(
                            psiElement(ASSIGNMENT_VALUE)
                                    .afterSibling(psiElement(VARIABLE_NAME).withText("context"))
                    )
            )

    /**
     * Matches the following:
     *
     * ```
     *    ${@ resourceType='<caret>'}
     * ```
     */
    val resourceTypeOptionAssignment: ElementPattern<PsiElement> =
            and(
                    stringLiteralValue,
                    psiElement().inside(psiElement(CONTEXT_EXPRESSION)),
                    psiElement().inside(
                            psiElement(ASSIGNMENT_VALUE)
                                    .afterSibling(psiElement(VARIABLE_NAME).withText("resourceType"))
                    )
            )

    /**
     * Matches the following:
     *
     * ```
     *    ${object.<caret>}
     * ```
     */
    val memberAccess: ElementPattern<PsiElement> =
            or(
                    psiElement().inside(psiElement(STRING_LITERAL))
                            .inside(psiElement(ARRAY_LIKE_ACCESS)),
                    psiElement(VAR_NAME).inside(psiElement(ACCESS_IDENTIFIER))
            )

    /**
     * Matches the following:
     *
     * ```
     *    data-sly-use="<caret>"
     *    data-sly-use.bean="<caret>"
     * ```
     */
    val dataSlyUseNoEl: ElementPattern<PsiElement> =
            psiElement()
                    .inside(xmlAttributeValue().withLocalName(
                            or(
                                    string().equalTo(DATA_SLY_USE),
                                    string().startsWith("$DATA_SLY_USE.")
                            )
                    ))

    /**
     * Matches the following:
     *
     * ```
     *    data-sly-include="<caret>"
     * ```
     */
    val dataSlyIncludeNoEl: ElementPattern<PsiElement> =
            psiElement()
                    .inside(
                            xmlAttributeValue()
                                    .withLocalName(
                                            string()
                                                    .equalTo(DATA_SLY_INCLUDE)))

    /**
     * Matches Htl xml attribute
     */
    val htlAttribute: ElementPattern<PsiElement> =
            psiElement(XML_NAME).withParent(xmlAttribute().withName(
                    or(
                            string().oneOfIgnoreCase(*HTL_ATTRIBUTES.toTypedArray()),
                            string().startsWith("$DATA_SLY_USE."),
                            string().startsWith("$DATA_SLY_TEST."),
                            string().startsWith("$DATA_SLY_LIST."),
                            string().startsWith("$DATA_SLY_REPEAT."),
                            string().startsWith("$DATA_SLY_TEMPLATE.")
                    )
            ))

    /**
     * Matches the following:
     *
     * ```
     *    data-sly-call="${<caret>}"
     * ```
     */
    val mainVariableInsideOfDataSlyCall: ElementPattern<PsiElement> =
            psiElement().inside(psiElement(HtlExpression::class.java))
                    .afterLeafSkipping(
                            psiElement(TokenType.WHITE_SPACE),
                            psiElement(EL_START))
                    .inside(psiElement().with(HtlTemplatePattern(DATA_SLY_CALL)))

    /**
     * Matches the following:
     *
     * ```
     *    data-sly-include="${'<caret>'}"
     * ```
     */
    val dataSlyIncludeMainString: ElementPattern<PsiElement> =
            and(
                    stringLiteralValue,
                    psiElement().afterLeafSkipping(
                            psiElement(TokenType.WHITE_SPACE),
                            psiElement(EL_START))
                            .inside(psiElement().with(HtlTemplatePattern(DATA_SLY_INCLUDE)))
            )

    /**
     * Matches the following:
     *
     * ```
     *    data-sly-use="${'<caret>'}"
     *    data-sly-use.bean="${'<caret>'}"
     * ```
     */
    val dataSlyUseMainString: ElementPattern<PsiElement> =
            and(
                    stringLiteralValue,
                    psiElement().afterLeafSkipping(
                            psiElement(TokenType.WHITE_SPACE),
                            psiElement(EL_START))
                            .inside(psiElement().with(HtlTemplatePattern(DATA_SLY_USE)))
            )

}

class HtlTemplatePattern(val name: String) : PatternCondition<PsiElement?>(name) {
    override fun accepts(element: PsiElement, context: ProcessingContext?): Boolean {
        return element.findParentByType(HtlHtlEl::class.java)
                ?.isInsideOf(name)
                ?: false
    }
}