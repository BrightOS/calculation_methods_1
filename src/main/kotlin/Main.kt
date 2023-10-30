const val PRECISION = 6

//val (a, b, c, d) = (1.0, -1.4, 0.01, 0.11)

data class Data(
    val name: String,
    val a: Double,
    val b: Double,
    val c: Double,
    val d: Double
) {
    var point = Point()
    var gradient = gradientF()
}

data class Point(
    val x: Double = 0.0,
    val y: Double = 0.0
)

// Исходные данные
val data = listOf(
    Data(name = "Вариант 1", a = 1.0, b = -1.4, c = 0.01, d = 0.11),
    Data(name = "Вариант 14", a = 14.0, b = -0.1, c = 1.69, d = 0.24)
)

val epsilon = 0.0001

// Длина шага
var alpha = 1.0

// Сама функция
fun Data.f(point: Point = this.point) =
    a * point.x + b * point.y + Math.exp(c * point.x * point.x + d * point.y * point.y)

// Её производная по X
fun Data.derivativeX() =
    a + 2 * c * point.x * Math.exp(c * point.x * point.x + d * point.y * point.y)

// Её производная по Y
fun Data.derivativeY() =
    b + 2 * d * point.y * Math.exp(c * point.x * point.x + d * point.y * point.y)

// Координаты вектора градиента конкретной точки
fun Data.gradientF() =
    Point(derivativeX(), derivativeY())

// Обновление градиента конкретного набора данных
fun Data.updateGradient() {
    gradient = gradientF()
}

// Оппределение следующей точки
fun Data.nextPoint() =
    gradientF().let {
        Point(
            x = point.x - alpha * it.x,
            y = point.y - alpha * it.y
        )
    }

// Установка следующей точки в качестве оперируемой в конкретном наборе данных
fun Data.updateNextPoint() {
    point = nextPoint()
}

// Длина градиента
fun Data.gradientLength() =
    Math.sqrt(gradient.x * gradient.x + gradient.y * gradient.y)

fun main() {
    data.forEach {
        with(it) {
            println("Работа с данными: $this")

            // Пока не выполняется критерий окончания счёта
            while (gradientLength() >= epsilon) {
                // Проверка на соответствие спуску. Если же значение возрастает, уменьшаем шаг
                if (f(nextPoint()) >= f()) {
                    alpha = alpha / 2
                    updateGradient()
                } else {
                    println(
                        "x = ${String.format("%.${PRECISION}f", point.x)}\t" +
                                "y = ${String.format("%.${PRECISION}f", point.y)}\t" +
                                "f = ${String.format("%.${PRECISION}f", f())}\t" +
                                "dF/dX = ${String.format("%.${PRECISION}f", gradient.x)}\t" +
                                "dF/dY = ${String.format("%.${PRECISION}f", gradient.y)}\t" +
                                "alpha = ${String.format("%.${PRECISION}f", alpha)}"
                    )

                    // Обновляем оперируемую точку
                    updateNextPoint()
                    // И градиент
                    updateGradient()
                }
            }

            // И выводим результаты
            println("Минимальная f = f(${point.x}, ${point.y}) = ${f()}")
            println()
        }
    }
}