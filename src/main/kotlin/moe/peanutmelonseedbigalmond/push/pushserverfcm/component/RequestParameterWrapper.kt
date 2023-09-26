package moe.peanutmelonseedbigalmond.push.pushserverfcm.component

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

class RequestParameterWrapper(request:HttpServletRequest):HttpServletRequestWrapper(request) {
    private val params= mutableMapOf<String,Array<String>>()
    init {
        this.params.putAll(request.parameterMap)
    }

    private fun addParameter(name:String,value:Any){
        if (value is Array<*> && value.isArrayOf<String>()){
            params[name] = value as Array<String>
        }else if (value is String){
            params[name] = arrayOf(value)
        }else{
            params[name]= arrayOf(value.toString())
        }
    }

    fun addParameters(extraParams:Map<String,Any>){
        extraParams.forEach { (k, v) ->addParameter(k,v) }
    }

    fun removeParameter(name:String){
        params.remove(name)
    }

    override fun getParameterValues(name: String?): Array<String> {
        return params[name]?: emptyArray()
    }

    override fun getParameter(name: String?): String {
        return params[name]?.get(0)?:""
    }
}