package jgappsandgames.me.save.tasks

import jgappsandgames.me.save.utility.pool.ComplexPool
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class Task: ComplexPool.PoolObject {
    companion object {
        // Save Constants --------------------------------------------------------------------------
        private const val PARENT = "parent"
        private const val VERSION = "version"
        private const val META = "meta"
        private const val TYPE = "type"
        private const val TASK_ID = "id"

        private const val CAL_CREATE = "cal_a"
        private const val CAL_DUE = "cal_b"
        private const val CAL_UPDATE = "cal_c"
        private const val CAL_ARCHIVED = "cal_d"
        private const val CAL_DELETED = "cal_e"

        private const val TITLE = "title"
        private const val NOTE = "note"
        private const val TAGS = "tags"
        private const val CHILDREN = "children"
        private const val CHECKPOINTS = "checkpoints"
        private const val STATUS = "status"
        private const val PRIORITY = "priority"

        private const val COMPLETED_ON_TIME = "completed_on_time"
        private const val COMPLETED_LATE = "completed_late"

        private const val PARENT_12 = "b"
        private const val VERSION_12 = "a"
        private const val META_12 = "c"
        private const val TYPE_12 = "d"
        private const val TASK_ID_12 = "e"

        private const val CAL_CREATE_12 = "f"
        private const val CAL_DUE_12 = "g"
        private const val CAL_UPDATE_12 = "h"
        private const val CAL_ARCHIVED_12 = "i"
        private const val CAL_DELETED_12 = "j"

        private const val TITLE_12 = "k"
        private const val NOTE_12 = "l"
        private const val TAGS_12 = "m"
        private const val CHILDREN_12 = "n"
        private const val CHECKPOINTS_12 = "o"
        private const val STATUS_12 = "p"
        private const val PRIORITY_12 = "q"

        private const val COMPLETED_ON_TIME_12 = "r"
        private const val COMPLETED_LATE_12 = "s"

        private const val BACKGROUND_COLOR_12 = "t"
        private const val FOREGROUND_COLOR_12 = "u"
        private const val FILENAME_12 = "v"

        private const val FILENAME_13 = "v"
        private const val PARENT_13 = "b"
        private const val VERSION_13 = "a"
        private const val META_13 = "c"
        private const val TYPE_13 = "d"
        private const val TASK_ID_13 = "e"

        private const val CAL_CREATE_13 = "f"
        private const val CAL_DUE_13 = "g"
        private const val CAL_UPDATE_13 = "h"
        private const val CAL_ARCHIVED_13 = "i"
        private const val CAL_DELETED_13 = "j"

        private const val DAYS_13 = "x"

        private const val TITLE_13 = "k"
        private const val NOTE_13 = "l"
        private const val TAGS_13 = "m"
        private const val CHILDREN_13 = "n"
        private const val CHECKPOINTS_13 = "o"
        private const val SHOPPING_LIST = "w"
        private const val STATUS_13 = "p"
        private const val PRIORITY_13 = "q"

        private const val COMPLETED_ON_TIME_13 = "r"
        private const val COMPLETED_LATE_13 = "s"

        private const val BACKGROUND_COLOR_13 = "t"
        private const val FOREGROUND_COLOR_13 = "u"

        // Type Constants --------------------------------------------------------------------------
        const val TYPE_NONE = 0
        const val TYPE_FOLDER = 1
        const val TYPE_TASK = 2
        const val TYPE_NOTE = 3
        const val TYPE_SHOPPING_LIST = 4
        const val TYPE_DAILY = 5

        // Priority Constants ----------------------------------------------------------------------
        const val DEFAULT_PRIORITY = 40
        const val STARRED_PRIORITY = 100
        const val HIGH_PRIORITY = 70
        const val NORMAL_PRIORITY = 30
        const val LOW_PRIORITY = 1
        const val IGNORE_PRIORITY = 0

        // Status Constants ------------------------------------------------------------------------
        const val STATUS_DONE = 10

        // Condition Constants ---------------------------------------------------------------------
        const val CON_NONE = 0
        const val CON_ACTIVE = 1
        const val CON_ARCHIVE = 2
        const val CON_DELETE = 3

        // Day Constants ---------------------------------------------------------------------------
        const val DAY_NONE = 0
        const val DAY_MONDAY = 1
        const val DAY_TUESDAY = 2
        const val DAY_WEDNESDAY = 3
        const val DAY_THURSDAY = 4
        const val DAY_FRIDAY = 5
        const val DAY_SATURDAY = 6
        const val DAY_SUNDAY = 7
    }

    // Data ----------------------------------------------------------------------------------------
    private var filename: Field<String> = Field(0L, "")
    private var parent: Field<String> = Field(0L, "")
    private var version: Field<Int> = Field(0L, 0)
    private var meta: Field<JSONObject?> = Field(0L, null)
    private var type: Field<Int> = Field(0L, 0)
    private var taskID: Field<Long> = Field(0L, 0L)
    private var condition: Field<Int> = Field(0L, 0)

    private var dateCreate: Field<Calendar?> = Field(0L, null)
    private var dateDue: Field<Calendar?> = Field(0L, null)
    private var dateUpdated: Field<Calendar?> = Field(0L, null)
    private var dateArchived: Field<Calendar?> = Field(0L, null)
    private var dateDeleted: Field<Calendar?> = Field(0L, null)

    private var days: Field<Int> = Field(0L, 0)

    private var title: Field<String> = Field(0L, "")
    private var note: Field<String> = Field(0L, "")
    private var tags: Field<ArrayList<String>> = Field(0L, ArrayList())
    private var children: Field<ArrayList<String>> = Field(0L, ArrayList())
    private var checkpoints: Field<ArrayList<Checkpoint>> = Field(0L, ArrayList())
    private var shoppingList: Field<ArrayList<String>> = Field(0L, ArrayList())
    private var status: Field<Int> = Field(0L, 0)
    private var priority: Field<Int> = Field(0L, 0)

    private var completedOnTime: Field<Boolean> = Field(0L, false)
    private var completedLate: Field<Boolean> = Field(0L, false)

    private var backgroundColor: Field<Int> = Field(0L, 0)
    private var foregroundColor: Field<Int> = Field(0L, 0)

    // Holders -------------------------------------------------------------------------------------
    private var childrenHolder: ArrayList<Task>? = null

    // Loaders -------------------------------------------------------------------------------------
    fun load(filename: String, sort: Boolean): Task {

    }

    private fun load11(filename: String, sort: Boolean) {

    }

    private fun load12(filename: String, sort: Boolean) {

    }

    private fun load13(filename: String, sort: Boolean) {

    }

    fun load(data: JSONObject, sort: Boolean): Task {

    }

    private fun load11(data: JSONObject, sort: Boolean) {

    }

    private fun load12(data: JSONObject, sort: Boolean) {

    }

    private fun load13(data: JSONObject, sort: Boolean) {

    }

    fun load(parent: String, type: Int): Task {

    }

    // Management Methods --------------------------------------------------------------------------
    fun save(): Task {

    }

    private fun save11() {

    }

    private fun save12() {

    }

    private fun save13() {

    }

    fun archive(): Boolean {

    }

    fun unArchive(): Boolean {

    }

    fun delete(): Boolean {

    }

    fun unDelete(): Boolean {

    }

    fun search(search: String): Boolean {

    }

    fun upgrade(version: Int): Boolean {

    }

    private fun upgrade12() {

    }

    private fun upgrade13() {

    }

    // To Methods ----------------------------------------------------------------------------------
    fun toJSON(): JSONObject {

    }

    private fun toJSON11(): JSONObject {

    }

    private fun toJSON12(): JSONObject {

    }

    private fun toJSON13(): JSONObject {

    }

    fun toHeavyJSON(): JSONObject {

    }

    private fun toHeavyJSON11(): JSONObject {

    }

    private fun toHeavyJSON12(): JSONObject {

    }

    private fun toHeavyJSON13(): JSONObject {

    }

    // Pool Methods --------------------------------------------------------------------------------
    override fun destroy() {

    }

    // Class Methods -------------------------------------------------------------------------------
    fun sortTags() {

    }

    fun sortTasks() {

    }

    fun sortCheckpoints() {

    }

    fun sortShoppingList() {

    }

    // Getters -------------------------------------------------------------------------------------
    fun getFilename(): String {

    }

    fun getParent(): String {

    }

    fun getVersion(): Int {

    }

    fun getMeta(): JSONObject {

    }

    fun getType(): Int {

    }

    override fun getID(): Long {

    }

    fun getCondition(): Int {

    }

    fun getDateCreate(): Calendar? {

    }

    fun getDateCreateString(): String {

    }

    fun getDateDue(): Calendar? {

    }

    fun getDateDueString(): String {

    }

    fun getDateUpdated(): Calendar? {

    }

    fun getDateUpdatedString(): String {

    }

    fun getDateArchived(): Calendar? {

    }

    fun getDateArchivedString(): String {

    }

    fun getDateDeleted(): Calendar? {

    }

    fun getDateDeletedString(): String {

    }

    fun getDays(): Int {

    }

    fun isRepeatMonday(): Boolean {

    }

    fun isRepeatTuesday(): Boolean {

    }

    fun isRepeatWednesday(): Boolean {

    }

    fun isRepeatThursday(): Boolean {

    }

    fun isRepeatFriday(): Boolean {

    }

    fun isRepeatSaturday(): Boolean {

    }

    fun isRepeatSunday(): Boolean {

    }

    fun getTitle(): String {

    }

    fun getNote(): String {

    }

    fun getTags(): ArrayList<String> {

    }

    fun getTagsString(): String {

    }

    fun getChildren(): ArrayList<String> {

    }

    fun getChildrenTasks(): ArrayList<Task> {

    }

    fun getCheckpoints(): ArrayList<Checkpoint> {

    }

    fun getShoppingItems(): ArrayList<String> {

    }

    fun getShoppingItemsList(): ArrayList<ShoppingItem> {

    }

    fun getStatus(): Int {

    }

    fun inProgress(): Boolean {

    }

    fun isCompleted(): Boolean {

    }

    fun getPriority(): Int {

    }

    fun getCombinedTaskScore(): Int {

    }

    fun wasCompletedOnTime(): Int {

    }

    fun wasCompletedLate(): Int {

    }

    fun getBackgroundColor(): Int {

    }

    fun getForegroundColor(): Int {

    }

    // Setters -------------------------------------------------------------------------------------
    fun setFilename(newFilename: String): Boolean {

    }

    fun setParent(newParent: String): Boolean {

    }

    fun setMeta(newMeta: JSONObject): Task {

    }

    fun setType(newType: Int): Boolean {

    }

    fun setDateDue(date: Calendar?): Task {

    }

    fun setDays(newDays: Int): Boolean {

    }

    fun setTitle(newTitle: String): Task {

    }

    fun setNote(newNote: String): Task {

    }

    fun setTags(newTags: ArrayList<String>): Task {

    }

    fun setChildren(newChildren: ArrayList<String>): Boolean {

    }

    fun setCheckpoints(newCheckpoints: ArrayList<Checkpoint>): Task {

    }

    fun setShoppingList(newShoppingList: ArrayList<String>): Boolean {

    }

    fun setStatus(newStatus: Int): Task {

    }

    fun markComplete(done: Boolean = true): Task {

    }

    fun markIncomplete(done: Boolean = false): Task {

    }

    fun setPriority(newPriority: Int): Task {

    }

    // Manipulators --------------------------------------------------------------------------------
    fun putMeta(key: String, value: Boolean): Task {

    }

    fun putMeta(key: String, value: Int): Task {

    }

    fun putMeta(key: String, value: Long): Task {

    }

    fun putMeta(key: String, value: String): Task {

    }

    fun putMeta(key: String, value: JSONObject): Task {

    }

    fun putMeta(key: String, value: JSONArray): Task {

    }

    fun addDay(day: Int): Task {

    }

    fun removeDay(day: Int): Task {

    }

    fun addTag(tag: String): Boolean {

    }

    fun removeTag(tag: String): Boolean {

    }

    fun addChild(child: String): Task {

    }

    fun addChild(child: Task): Task {

    }

    fun removeChild(child: String): Task {

    }

    fun removeChild(child: Task): Task {

    }

    fun addCheckpoint(checkpoint: Checkpoint): Task {

    }

    fun editCheckpoint(checkpoint: Checkpoint): Task {

    }

    fun removeCheckpoint(checkpoint: Checkpoint): Task {

    }

    fun addShoppingItem(shoppingItem: String): Task {

    }

    fun addShoppingItem(shoppingItem: ShoppingItem): Task {

    }

    fun removeShoppingItem(shoppingItem: String): Task {

    }

    fun removeShoppingItem(shoppingItem: ShoppingItem): Task {

    }
}

class Checkpoint(var id: Int, var text: String, var status: Boolean = false): ComplexPool.PoolObject {
    companion object {
        // Save Constants --------------------------------------------------------------------------
        const val ID = "position"
        const val STATUS = "status"
        const val TEXT = "text"
    }
    // Initializers --------------------------------------------------------------------------------
    constructor(data: JSONObject): this(data.optInt(ID, 0), data.optString(TEXT, ""), data.optBoolean(STATUS, false))

    // Loaders -------------------------------------------------------------------------------------
    fun load(id: Int, text: String, status: Boolean = false): Checkpoint {

    }

    fun load(data: JSONObject): Checkpoint {

    }

    // To Methods ----------------------------------------------------------------------------------
    fun toJSON(): JSONObject {

    }

    override fun toString(): String {

    }

    // Pool Methods --------------------------------------------------------------------------------
    override fun destroy() {

    }

    override fun getID(): Long {

    }
}

class ShoppingItem: ComplexPool.PoolObject {
    companion object {
        // Save Constants --------------------------------------------------------------------------
        private const val FILENAME = "a"
        private const val VERSION = "b"
        private const val META = "c"

        private const val CAL_CREATE = "d"
        private const val CAL_DUE = "e"
        private const val CAL_UPDATED = "f"
        private const val CAL_ARCHIVED = "g"
        private const val CAL_DELETED = "h"

        private const val TITLE = "i"
        private const val NOTE = "j"
        private const val TAGS = "k"
        private const val PRICES = "l"
        private const val COLLECTED = "m"
    }

    // Data ----------------------------------------------------------------------------------------
    private var filename: Field<String> = Field(0L, "")
    private var version: Field<Int> = Field(0L, 0)
    private var meta: Field<JSONObject> = Field(0L, JSONObject())

    private var dateCreate: Field<Calendar?> = Field(0L, null)
    private var dateUpdated: Field<Calendar?> = Field(0L, null)
    private var dateArchived: Field<Calendar?> = Field(0L, null)
    private var dateDeleted: Field<Calendar?> = Field(0L, null)

    private var title: Field<String> = Field(0L, "")
    private var note: Field<String> = Field(0L, "")
    private var tags: Field<ArrayList<String>> = Field(0L, ArrayList())
    private var prices: Field<ArrayList<Price>> = Field(0L, ArrayList())
    private var collected: Field<Boolean> = Field(0L, false)

    // Loaders -------------------------------------------------------------------------------------
    fun load(): ShoppingItem {

    }

    private fun load13() {

    }

    fun load(filename: String): ShoppingItem {

    }

    private fun load13(filename: String) {

    }

    fun load(data: JSONObject): ShoppingItem {

    }

    private fun load13(data: JSONObject) {

    }

    // Management Methods --------------------------------------------------------------------------
    fun save(): Task {

    }

    private fun save13() {

    }

    private fun archive(): Boolean {

    }

    private fun unArchive(): Boolean {

    }

    private fun delete(): Boolean {

    }

    private fun unDelete(): Boolean {

    }

    fun search(search: String): Boolean {

    }

    fun upgrade(version: Int): Boolean {

    }

    fun upgrade13() {

    }

    // To Methods ----------------------------------------------------------------------------------
    fun toJSON(): JSONObject {

    }

    override fun toString(): String {
        return super.toString()
    }

    // Pool Methods --------------------------------------------------------------------------------
    override fun destroy() {

    }

    override fun getID(): Long {

    }

    // Private Class Methods -----------------------------------------------------------------------
    fun sortTags() {

    }

    // Getters -------------------------------------------------------------------------------------
    fun getFilename(): String {

    }

    fun getVersion(): Int {

    }

    fun getMeta(): JSONObject {

    }

    fun getDateCreate(): Calendar? {

    }

    fun getDateCreateString(): String {

    }

    fun getDateDue(): Calendar? {

    }

    fun getDateDueString(): String {

    }

    fun getDateUpdated(): Calendar? {

    }

    fun getDateUpdatedString(): String {

    }

    fun getDateArchived(): Calendar? {

    }

    fun getDateArchivedString(): String {

    }

    fun getDateDeleted(): Calendar? {

    }

    fun getDateDeletedString(): String {

    }

    fun getTitle(): String {

    }

    fun getNote(): String {

    }

    fun getTags(): ArrayList<String> {

    }

    fun getPrices(): ArrayList<Price> {

    }

    fun isCollected(): Boolean {

    }

    // Setters -------------------------------------------------------------------------------------
    fun setFilename(newFilename: String): Boolean {

    }

    fun setMeta(newMeta: JSONObject): ShoppingItem {

    }

    fun setTitle(newTitle: String): ShoppingItem {

    }

    fun setNote(newNote: String): ShoppingItem {

    }

    fun setTags(newTags: ArrayList<String>): ShoppingItem {

    }

    fun setPrices(newPrices: ArrayList<Price>): ShoppingItem {

    }

    fun setCollected(collected: Boolean): ShoppingItem {

    }

    // Manipulators --------------------------------------------------------------------------------
    fun putMeta(key: String, value: Boolean): ShoppingItem {

    }

    fun putMeta(key: String, value: Int): ShoppingItem {

    }

    fun putMeta(key: String, value: Long): ShoppingItem {

    }

    fun putMeta(key: String, value: String): ShoppingItem {

    }

    fun putMeta(key: String, value: JSONObject): ShoppingItem {

    }

    fun putMeta(key: String, value: JSONArray): ShoppingItem {

    }

    fun addTag(tag: String): Boolean {

    }

    fun removeTag(tag: String): Boolean {

    }

    fun addPrice(price: Price): ShoppingItem {

    }

    fun removePrice(price: Price): ShoppingItem {

    }

    // Internal Classes ----------------------------------------------------------------------------
    class Price(text: String, price: Float) {
        fun toJSON(): JSONObject {

        }
    }
}

class Field<T>(var timeStamp: Long, var field: T)