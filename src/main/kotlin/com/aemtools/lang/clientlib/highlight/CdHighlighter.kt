package com.aemtools.lang.clientlib.highlight

import com.aemtools.lang.clientlib.lexer.CdLexer
import com.aemtools.lang.clientlib.psi.CdTypes
import com.intellij.lexer.LayeredLexer
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as dc
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey as r

/**
 * @author Dmytro_Troynikov
 */
class CdHighlighter : SyntaxHighlighterBase() {
    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
        return pack(map(tokenType))
    }

    private fun map(tokenType: IElementType?) = when (tokenType) {
        CdTypes.COMMENT_TOKEN -> CdColors.COMMENT
        CdTypes.PREFIX_TOKEN -> CdColors.PREFIX
        CdTypes.DD, CdTypes.DOT -> CdColors.DOT
        CdTypes.SEPARATOR,
        CdTypes.WORD -> CdColors.IMPORT
        else -> null
    }

    override fun getHighlightingLexer(): Lexer = LayeredLexer(CdLexer())

}

object CdColors {
    val COMMENT = r("cd.COMMENT", dc.LINE_COMMENT)
    val IMPORT = r("cd.IMPORT", dc.IDENTIFIER)
    val DOT = r("cd.DOT", dc.DOT)
    val PREFIX = r("cd.PREFIX", dc.CONSTANT)
}