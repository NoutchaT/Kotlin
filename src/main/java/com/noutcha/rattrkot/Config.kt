package com.noutcha.rattrkot

data class Config(
    var annee: Int,
    var appareil: String,
    var commande: Number,
    var impressions: Int,
    var clics: Int,
    var couts: Number,
    var chiffreAffaire: Number,
    var mois: String
) {

    override fun toString(): String {
        return "[annee=" + annee + ", appareil=" + appareil + ", commande=" + commande + ", " +
                "impressions=" + impressions + ", clics=" + clics + ", couts=" + couts + ", " +
                "chiffreAffaire=" + chiffreAffaire + ", mois=" + mois + "]"
    }

}