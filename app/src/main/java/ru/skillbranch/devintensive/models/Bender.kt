package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    var errorCount: Int = 0
    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        val (isValid, message) = question.validateAnswer(answer)
        return if (isValid) {
            return if (question.answers.contains(answer.toLowerCase())) {
                question = question.nextQuestion()
                "Отлично - ты справился\n${question.question}" to status.color
            } else {
                errorCount++
                if (errorCount == 3) {
                    status = Status.NORMAL
                    question = Question.NAME
                    errorCount = 0
                    "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
                } else {
                    status = status.nextStatus()
                    "Это неправильный ответ\n${question.question}" to status.color
                }
            }
        } else {
            return "$message\n${question.question}" to status.nextStatus().color
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun validateAnswer(answer: String): Pair<Boolean, String> =
                (answer != null && answer.isNotEmpty() && answer[0].isUpperCase()) to "Имя должно начинаться с заглавной буквы"

            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun validateAnswer(answer: String): Pair<Boolean, String> =
                (answer != null && answer.isNotEmpty() && answer[0].isLowerCase()) to "Профессия должна начинаться со строчной буквы"

            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun validateAnswer(answer: String): Pair<Boolean, String> =
                (answer != null && answer.isNotEmpty() && !answer.contains(Regex("[0-9]"))) to "Материал не должен содержать цифр"

            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun validateAnswer(answer: String): Pair<Boolean, String> =
                (answer != null && answer.isNotEmpty() && answer.contains(Regex("^\\d+\$"))) to "Год моего рождения должен содержать только цифры"

            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun validateAnswer(answer: String): Pair<Boolean, String> =
                (answer != null && answer.isNotEmpty() && answer.contains(Regex("^\\d{7}$"))) to "Серийный номер содержит только цифры, и их 7"

            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun validateAnswer(answer: String): Pair<Boolean, String?> = Pair(true, null)

            override fun nextQuestion(): Question = NAME
        };

        abstract fun nextQuestion(): Question
        abstract fun validateAnswer(answer: String): Pair<Boolean, String?>

    }


}