package com.aemtools.constant

/**
 * @author Dmytro_Troynikov
 */
object const {

    val AEMTOOLS: String = "aemtools"

    val IDEA_STRING_CARET_PLACEHOLDER: String = "IntellijIdeaRulezzz"

    val XTYPE : String = "xtype"

    val JCR_PRIMARY_TYPE : String = "jcr:primaryType"

    val DIALOG_XML : String = "dialog.xml"

    val CQ_EDITCONFIG_XML : String = "_cq_editConfig.xml"

    val  REP_POLICY: String = "_rep_policy.xml"

    val SLY_TAG: String = "sly"

    object htl {
        val HTL_PREFIX = "data-sly-"
        val DATA_SLY_USE = "${HTL_PREFIX}use"
        val DATA_SLY_TEST = "${HTL_PREFIX}test"
        val DATA_SLY_REPEAT = "${HTL_PREFIX}repeat"
        val DATA_SLY_LIST = "${HTL_PREFIX}list"
        val DATA_SLY_ATTRIBUTE = "${HTL_PREFIX}attribute"
        val DATA_SLY_ELEMENT = "${HTL_PREFIX}element"
        val DATA_SLY_CALL = "${HTL_PREFIX}call"
        val DATA_SLY_INCLUDE = "${HTL_PREFIX}include"
        val DATA_SLY_UNWRAP = "${HTL_PREFIX}unwrap"
        val DATA_SLY_TEXT = "${HTL_PREFIX}text"
        val DATA_SLY_RESOURCE = "${HTL_PREFIX}resource"
        val HTL_ATTRIBUTES = listOf(
                DATA_SLY_USE,
                DATA_SLY_TEST,
                DATA_SLY_REPEAT,
                DATA_SLY_LIST,
                DATA_SLY_ATTRIBUTE,
                DATA_SLY_ELEMENT,
                DATA_SLY_CALL,
                DATA_SLY_INCLUDE,
                DATA_SLY_UNWRAP,
                DATA_SLY_TEXT,
                DATA_SLY_RESOURCE
        )
        // Attributes, which can meet in tag only one time
        val UNIQUE_HTL_ATTRIBUTES = listOf(
                DATA_SLY_LIST,
                DATA_SLY_UNWRAP,
                DATA_SLY_REPEAT
        )
    }

    object js {
        object type {
            val STRING : String = "String"
            val BOOLEAN : String = "Boolean"
            val ARRAY : String = "Array"
            val FUNCTION : String = "Function"
        }
    }

    object xml {
        val XML_ATTRIBUTE_NAME : String = "XML_NAME"
        val XML_ATTRIBUTE_VALUE : String = "XML_ATTRIBUTE_VALUE_TOKEN"
        val XML_TAG_NAME : String = "XML_TAG_NAME"
    }

}
