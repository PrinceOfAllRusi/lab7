package tools

import multilib.utilities.commandsData.Token

class DataList {
    private var scriptList: ArrayList<String>
    private var tokenList: ArrayList<Token>
    constructor() {
        scriptList = ArrayList()
        tokenList = ArrayList()
    }
    constructor(scriptList: ArrayList<String>, tokenList: ArrayList<Token>) {
        this.scriptList = scriptList
        this.tokenList = tokenList
    }
    fun getScriptList() = scriptList
    fun getTokenList() = tokenList
    fun getTokenFromTokenList(tokenName: String): Token {
        var concreteToken = Token()
        for (token in tokenList) {
            if (token.getToken() == tokenName) {
                concreteToken = token
                break
            }
        }
        return concreteToken
    }
}