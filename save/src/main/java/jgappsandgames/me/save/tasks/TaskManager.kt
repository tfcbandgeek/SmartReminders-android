package jgappsandgames.me.save.tasks

import org.json.JSONArray
import org.json.JSONObject

// Constants ---------------------------------------------------------------------------------------
// Pools -------------------------------------------------------------------------------------------
// Data --------------------------------------------------------------------------------------------
// Management Methods ------------------------------------------------------------------------------
fun createTasks() {

}

fun forceCreateTasks() {

}

fun loadTasks() {

}

fun saveTasks() {

}

fun cleanCacheTasks() {

}

fun cleanSave() {

}

fun upgrade(version: Int) {

}

// Getters -----------------------------------------------------------------------------------------
fun getHome(): ArrayList<String> {

}

fun getAll(): ArrayList<String> {

}

fun getArchived(): ArrayList<String> {

}

fun getDeleted(): ArrayList<String> {

}

fun getHomeTasks(): ArrayList<Task> {

}

fun getAllTasks(): ArrayList<Task> {

}

fun getArchivedTasks():ArrayList<Task> {

}

fun getDeletedTasks(): ArrayList<Task> {

}

// Task Handling Methods ---------------------------------------------------------------------------
fun getTask(filename: String): Task {

}

fun getTaskList(files: ArrayList<String>): ArrayList<Task> {

}

fun getCheckpoint(data: JSONObject): Checkpoint {

}

fun getCheckpoints(data: JSONArray): ArrayList<Checkpoint> {

}