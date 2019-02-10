package com.noutcha.rattrkot

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

private val ANNEE = 0
private val APPAREIL = 1
private val COMMANDE = 2
private val IMPRESSIONS = 3
private val CLICS = 4
private val COUTS = 5
private val AFFAIRE = 7
private val MOIS = 9

fun main(args: Array<String>?) {
    var fileReader: BufferedReader? = null
    val anneeMap = mutableMapOf<Int, MutableMap<String, Number>>()
    val appareilMap = mutableMapOf<String, Number>()
    val appareilmoisMap = mutableMapOf<String, MutableMap<String, Number>>()

    try {
        val config = ArrayList<Config>()
        var line: String?

        fileReader = BufferedReader(FileReader("data_csv.csv"))

        // Lire CSV
        fileReader.readLine()

        line = fileReader.readLine()
        while (line != null) {
            val tokens = line.split(";")


            line = fileReader.readLine()

        if (tokens.isNotEmpty()) {
            val conf = Config(
                Integer.parseInt(tokens[ANNEE]),
                tokens[APPAREIL],
                NumberFormat.getNumberInstance(Locale.FRANCE).parse(tokens[COMMANDE]),
                Integer.parseInt(tokens[IMPRESSIONS]),
                Integer.parseInt(tokens[CLICS]),
                NumberFormat.getNumberInstance(Locale.FRANCE).parse(tokens[COUTS]),
                NumberFormat.getNumberInstance(Locale.FRANCE).parse(tokens[AFFAIRE]),
                tokens[MOIS]
            )
            config.add(conf)
        }
            line = fileReader.readLine()
    }

        for (ligne in config) {
            anneeSet(anneeMap, ligne.annee, ligne.mois, ligne.chiffreAffaire)
            appareilSet(appareilMap, ligne.appareil, ligne.chiffreAffaire)
            appareilmoisSet(appareilmoisMap, ligne.appareil, ligne.mois, ligne.chiffreAffaire)
        }


        //panier moyen
        val CAtotal = config.map { it.commande.toFloat() }.sum()


        //coût par clic
        val clicTotal = config.map { it.clics.toFloat() }.sum()
        val coutTotal = config.map { it.couts.toFloat() }.sum()



        //taux de clic
        val clicTotal2 = config.map { it.clics }.sum()
        val imprTotal = config.map { it.impressions }.sum()
        val rate = formatFloat("%.2f", (clicTotal2.toFloat() / imprTotal.toFloat()) * 100)

        //ROI
        val CAtotal2 = config.map { it.chiffreAffaire.toFloat() }.sum()
        val coutTotal2 = config.map { it.couts.toFloat() }.sum()
        val roi = formatFloat("%.2f", CAtotal2 / coutTotal2)

        //chiffre d’affaires par mois par année
        println("\nLe chiffre d’affaires par mois:")
        for ((annee, moisMap) in anneeMap) {
            for ((mois, chiffreAffaire) in moisMap) {
                println("En $annee pour le mois de $mois  =  " + formatFloat("%.2f", chiffreAffaire) + "€")
            }
        }

        //chiffre d’affaires par appareil
        println("\nLe chiffre d’affaires par appareil :")
        for ((appareil, chiffreAffaire) in appareilMap) {
            println("Pour les $appareil  =  " + formatFloat("%.2f", chiffreAffaire) + "€")
        }

        //Le ROI segmenté par appareil et par mois
        println("\nLe ROI segmenté par appareil par mois :")

        for ((appareil, moisMap) in appareilmoisMap) {
            for ((mois, chiffreAffaire) in moisMap) {
                var annee = 2017
                println("Pour les $appareil, au mois de $mois $annee  =  " + formatFloat("%.2f", chiffreAffaire) + "€")
            }
        }

        //print
        println("\nLe coût par clic est de " + formatFloat("%.2f", coutTotal / clicTotal) + "€")
        println("\nLe taux de clic est de $rate%")
        println("\nLe panier moyen est de " + formatFloat("%.2f", CAtotal / config.size) + "€")
        println("\nLe ROI est de $roi%")

        
    } catch (e: Exception) {
        println("Reading CSV Error!")
        e.printStackTrace()
    } finally {
        try {
            fileReader!!.close()
        } catch (e: IOException) {
            println("Closing fileReader Error!")
            e.printStackTrace()
        }
    }


}



fun formatFloat(format: String, value: Number): String {
    return format.format(value)
}

fun anneeSet( map: MutableMap<Int, MutableMap<String, Number>>,annee: Int, mois: String,value: Number)
{
    var anneeMap = mutableMapOf<String, Number>()
    var chiffreAffaire: Number = 0

    if (map.contains(annee)) anneeMap = map[annee]!!
    if (anneeMap.contains(mois)) chiffreAffaire = anneeMap[mois]!!

    anneeMap[mois] = chiffreAffaire.toFloat() + value.toFloat()
    map[annee] = anneeMap
}

fun appareilmoisSet(map: MutableMap<String, MutableMap<String, Number>>,appareil: String,mois: String,value: Number)
{
    var moisMap = mutableMapOf<String, Number>()
    var chiffreAffaire: Number = 0

    if (map.contains(appareil)) moisMap = map[appareil]!!
    if (moisMap.contains(mois)) chiffreAffaire = moisMap[mois]!!

    moisMap[mois] = chiffreAffaire.toFloat() + value.toFloat()
    map[appareil] = moisMap
}

fun appareilSet(map: MutableMap<String, Number>, appareil: String, value: Number) {
    var chiffreAffaire: Number = 0

    if (map.contains(appareil)) chiffreAffaire = map[appareil]!!

    map[appareil] = chiffreAffaire.toFloat() + value.toFloat()
}
