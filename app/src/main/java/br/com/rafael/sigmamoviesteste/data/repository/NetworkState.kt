package br.com.rafael.sigmamoviesteste.data.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(val status: Status, val msg:String){

    companion object{

        var LOADED: NetworkState
        var LOADING: NetworkState
        var ERROR: NetworkState
        var ENDOFLIST: NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS, "Sucesso")
            LOADING = NetworkState(Status.RUNNING, "Processando")
            ERROR = NetworkState(Status.FAILED, "Alguma coisa deu errado")
            ENDOFLIST = NetworkState(Status.FAILED, "Você chegou no fim da lista")
        }

    }

}