package voroby.app

class Greeting {
    fun greeting(): String {
        return "Guess what is it platform? > ${Platform().platform.reversed()}!"
    }
}