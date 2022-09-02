package com.hfy.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class HufeiyangPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {

        println "HufeiyangPlugin name ${project.name}"

        project.tasks.create("hfyTask") {
            group "hufeiyang"
            doLast {
                println "HufeiyangPlugin hfyTask doLast!"
            }
        }

//        project.tasks.create('hfyTask2', HfyTask.class).mustRunAfter  hfyTask

    }
}