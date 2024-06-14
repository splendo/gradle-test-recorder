package rs.houtbecke.gradle.recorder.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.registerIfAbsent

const val RECORD_EXTENSION_NAME = "recordConfig"

const val START_RECORD_ANDROID_TASK_NAME = "recordAndroid"
const val STOP_RECORD_ANDROID_TASK_NAME = "stopRecordAndroid"

abstract class RecorderPlugin : Plugin<Project> {
    override fun apply(project: Project) {
         val recordExtension = project.extensions.create(RECORD_EXTENSION_NAME, RecordExtension::class, project)

         project.gradle.sharedServices.registerIfAbsent("AndroidRecorderService", AndroidRecorderService::class) {}

         project.tasks.register(START_RECORD_ANDROID_TASK_NAME, StartRecordTask::class) {
             videoOutput.set(recordExtension.videoOutput)
         }

         project.tasks.register(STOP_RECORD_ANDROID_TASK_NAME, StopRecordTask::class)
    }
}
