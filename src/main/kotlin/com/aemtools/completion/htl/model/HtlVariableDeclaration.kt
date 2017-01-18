package com.aemtools.completion.htl.model

import com.intellij.psi.xml.XmlAttribute

/**
 * Represents the name of variable and the [XmlAttribute] in which it was declared.
 *
 * @author Dmytro_Troynikov
 */
data class HtlVariableDeclaration(
        /**
         * Declaration [XmlAttribute].
         */
        val xmlAttribute: XmlAttribute,
        /**
         * The name of variable.
         */
        val variableName: String,
        /**
         * The [DeclarationType] of current variable.
         */
        val type: DeclarationType = DeclarationType.VARIABLE,
        /**
         * Certain declarations may have own [ResolutionResult]. By default the result is empty.
         */
        val resolutionResult: ResolutionResult = ResolutionResult())