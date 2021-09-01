package wsdydeni.widget.custom.until

import kotlin.random.Random

class RandomUtils {
    companion object {
        fun randomFloat(min : Float,max : Float) : Float {
            if (min > max) throw IllegalArgumentException("min must less-then max")
            return min + Random.nextFloat() * (max - min)
        }
    }
}