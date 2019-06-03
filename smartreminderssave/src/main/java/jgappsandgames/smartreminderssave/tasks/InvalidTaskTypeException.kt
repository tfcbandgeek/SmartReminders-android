package jgappsandgames.smartreminderssave.tasks

class InvalidTaskTypeException(type: Int, task: Boolean = false, folder: Boolean = false, note: Boolean = false, shopping_list: Boolean = false):
        Exception(buildMessage(type, task, folder, note, shopping_list)) {
    companion object {
        private fun buildMessage(type: Int, task: Boolean, folder: Boolean, note: Boolean, shopping_list: Boolean): String {
            var temp = "Object of type [$type] is not compatible with this action*. This action is usable with: "
            if (task) temp = "$temp Task,"
            if (folder) temp = "$temp Folder,"
            if (note) temp = "$temp Note,"
            if (shopping_list) temp = "$temp Shopping List"
            return temp
        }
    }
}