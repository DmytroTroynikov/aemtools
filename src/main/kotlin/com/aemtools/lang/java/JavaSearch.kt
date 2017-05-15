package com.aemtools.lang.java

import com.aemtools.constant.const
import com.aemtools.constant.const.java.POJO_USE
import com.aemtools.constant.const.java.USE_INTERFACE
import com.aemtools.constant.const.java.WCM_USE_CLASS
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiAnonymousClass
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiModifier
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.AnnotatedElementsSearch
import com.intellij.psi.search.searches.ClassInheritorsSearch

/**
 * Utility object for java search.
 * @author Dmytro Troynikov
 */
object JavaSearch {

    val USE_CLASSES = listOf(USE_INTERFACE, WCM_USE_CLASS, POJO_USE)

    /**
     * Search for [PsiClass] by qualified name with predefined "allScope".
     *
     * Shortcut for [JavaPsiFacade.findClass]
     * @param qualifiedName the full qualified name of class
     * @param project the project
     * @see JavaPsiFacade
     * @return [PsiClass] instance, __null__ if no instance was found
     */
    fun findClass(qualifiedName: String, project: Project)
            : PsiClass? =
            JavaPsiFacade.getInstance(project)
                    .findClass(qualifiedName, searchScope(project))

    /**
     * Search for inheritors of given [PsiClass].
     *
     * Shortcut for [ClassInheritorsSearch.search]
     * @see ClassInheritorsSearch
     * @param psiClass the base class
     * @param project the project
     * @return list of inheritors of given class
     */
    fun findInheritors(psiClass: PsiClass, project: Project): List<PsiClass> =
            ClassInheritorsSearch.search(psiClass, searchScope(project), true)
                    .findAll().toList()

    /**
     * Search classes annotated by given annotation.
     *
     * Shortcut for [AnnotatedElementsSearch.searchPsiClasses]
     * @see AnnotatedElementsSearch
     * @param annotation the type of annotation
     * @param project the project
     * @return list of annotated classes
     */
    fun findAnnotatedClasses(annotation: PsiClass, project: Project): List<PsiClass> =
            AnnotatedElementsSearch.searchPsiClasses(annotation, searchScope(project))
                    .findAll().toList()

    /**
     * Find all sling models in the project.
     * @param project the project
     * @return list of sling models
     */
    fun findSlingModels(project: Project): List<PsiClass> =
            findClass(const.java.SLING_MODEL, project)?.let {
                findAnnotatedClasses(it, project)
                        .filterNot { it is PsiAnonymousClass }
                        .filterNot { it.hasModifierProperty(PsiModifier.ABSTRACT) }
            }.orEmpty()

    /**
     * Find all __io.sightly.java.api.Use__ and __com.adobe.cq.sightly.WCMUse__
     * inheritors in given project.
     * @param project the project
     * @return list of inheritors
     */
    fun findWcmUseClasses(project: Project): List<PsiClass> = USE_CLASSES.map { findClass(it, project) }
            .filterNotNull()
            .flatMap { findInheritors(it, project) }
            .filterNot { it is PsiAnonymousClass }
            .filterNot { it.hasModifierProperty(PsiModifier.ABSTRACT) }
            .toSet()
            .toList()

    private fun searchScope(project: Project) = GlobalSearchScope.allScope(project)

}