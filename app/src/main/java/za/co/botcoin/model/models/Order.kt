package za.co.botcoin.model.models

class Order : BaseModel {
    var id: String = ""
    var type: String = ""
    var state: String = ""
    var limitPrice: String = ""
    var limitVolume: String = ""
    var pair: String = ""
    var createdTime: String = ""
    var completedTime: String = ""

    constructor(id: String, type: String, state: String, limitPrice: String, limitVolume: String, pair: String, createdTime: String, completedTime: String) {
        this.id = id
        this.type = type
        this.state = state
        this.limitPrice = limitPrice
        this.limitVolume = limitVolume
        this.pair = pair
        this.createdTime = createdTime
        this.completedTime = completedTime
    }
}