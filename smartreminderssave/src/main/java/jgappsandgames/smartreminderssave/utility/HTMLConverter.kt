package jgappsandgames.smartreminderssave.utility

import jgappsandgames.smartreminderssave.BuildConfig
import jgappsandgames.smartreminderssave.tasks.Task

/**
 * HTMLConverter
 * Created by joshua on 12/15/2017.
 *
 * Class used to Convert A Task to HTML and vice versa
 */
class HTMLConverter {
    companion object {
        // TODO: Tags
        // TODO: Status
        // TODO: DateDue
        // TODO: Priority
        // TODO: CheckPoints
        // TODO: Folder
        fun convertTaskToHTML(task: Task): String {
            val html = StringBuilder()
            val end = System.getProperty("line.separator")

            html.append("<html>").append(end)
            html.append("<head>").append(end)
            html.append("<title>").append(end)
            html.append("Smart Reminders ${BuildConfig.VERSION_NAME}").append(end)
            html.append("</title>").append(end)
            html.append("</head>").append(end)
            html.append("<body>").append(end)
            html.append("<h1>").append(end)
            html.append(task.getTitle()).append(end)
            html.append("</h1>").append(end)
            html.append("<h2>").append(end)
            html.append(task.getNote()).append(end)
            html.append("</h2>").append(end)
            html.append("</body>").append(end)
            html.append("</html>").append(end)
            return ""
        }

        fun convertHTMLToTask(html: String): Task? {
            return null
        }
    }
}