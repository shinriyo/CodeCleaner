package com.shinriyo.codecleaner

import com.intellij.codeInspection.ex.InspectionManagerEx
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.profile.codeInspection.InspectionProjectProfileManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.LocalInspectionToolSession

class ApplyAllInspectionsAction : AnAction("Apply All Inspections Fixes") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getRequiredData(com.intellij.openapi.actionSystem.CommonDataKeys.PSI_FILE)

        WriteCommandAction.runWriteCommandAction(project) {
            val inspectionManager = InspectionManagerEx.getInstance(project)
            val profile = InspectionProjectProfileManager.getInstance(project).currentProfile
            val inspectionTools = profile.getAllTools().filter { it.tool is LocalInspectionTool }
            
            val problems: List<ProblemDescriptor> = inspectionTools.flatMap { tool ->
                val holder = ProblemsHolder(inspectionManager, file, false)
                val visitor = (tool.tool as LocalInspectionTool).buildVisitor(holder, false)
                file.accept(visitor)
                holder.results
            }

            problems.forEach { problem: ProblemDescriptor ->
                problem.fixes?.firstOrNull()?.applyFix(project, problem)
            }
        }
    }
}
