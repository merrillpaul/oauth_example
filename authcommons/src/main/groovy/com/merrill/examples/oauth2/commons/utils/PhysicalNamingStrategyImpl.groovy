package com.merrill.examples.oauth2.commons.utils

import org.apache.commons.lang3.StringUtils
import org.hibernate.boot.model.naming.Identifier
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment

/**
 * Created by upaulm2 on 1/12/17.
 */
class PhysicalNamingStrategyImpl extends PhysicalNamingStrategyStandardImpl  {
    final def ABBREVIATIONS = [
            account: "acct",
            task   : "tsk"
    ]

    @Override
    Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
        def parts = splitAndReplace(name.text)

        // all sequences need 'seq' at the end
        if (!"seq".equalsIgnoreCase(parts.getLast())) {
            parts.add("seq");
        }
        context.identifierHelper.toIdentifier(join(parts), name.isQuoted())
    }

    @Override
    Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        def parts = splitAndReplace(name.text)
        parts.removeAll("_")
        context.identifierHelper.toIdentifier(join(parts), name.isQuoted())
    }

    @Override
    Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {

        if (name.text.toLowerCase().startsWith("hibernate")) {
            return super.toPhysicalTableName(name, context)
        }
        def parts = splitAndReplace(name.text)
        context.identifierHelper.toIdentifier(join(parts), name.isQuoted())
    }

    def join(parts) {
        parts.join("_")
    }


    def applyAbbreviationReplacement(String word) {
        if (ABBREVIATIONS.containsKey(word)) {
            return ABBREVIATIONS.get(word)
        }
        word
    }

    /**
     * Splits a camelCase to [camel, case] and replaces any abbreviations as per company policy
     * @param name
     * @return
     */
    def splitAndReplace(String name) {
        StringUtils.splitByCharacterTypeCamelCase(name).findAll {
            it != null && !it.trim().isEmpty()
        }.collect {
            applyAbbreviationReplacement(it.toLowerCase(Locale.ROOT))
        }
    }
}
