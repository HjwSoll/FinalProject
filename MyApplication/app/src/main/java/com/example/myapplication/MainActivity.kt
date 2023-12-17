package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory
import kotlinx.android.synthetic.main.activity_main.*


var text = ""

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)

        textView.text = ""

        // 키 값
        val key = "키값"
        val pageNo = "&pageNo=1"
        val numOfRows ="&numOfRows=5"
        val MobileOS = "&MobileOS=AND"
        val MobileApp = "&MobileApp=AppTest"
        val url = "https://openapi.gg.go.kr/PlaceThatDoATasteyFoodSt"+key+pageNo+numOfRows+MobileOS+MobileApp

        button.setOnClickListener {
            val thread = Thread(NetworkThread(url))
            thread.start()
            thread.join()

            textView.text = text
        }
    }

}

class NetworkThread(
    var url: String): Runnable {

    override fun run() {

        try {

            val xml : Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url)


            xml.documentElement.normalize()

            val list:NodeList = xml.getElementsByTagName("item")

            for(i in 0..list.length-1){

                val n:Node = list.item(i)

                if(n.getNodeType() == Node.ELEMENT_NODE){

                    val elem = n as Element

                    val map = mutableMapOf<String,String>()


                    for(j in 0..elem.attributes.length - 1) {

                        map.putIfAbsent(elem.attributes.item(j).nodeName, elem.attributes.item(j).nodeValue)

                    }


                    println("=========${i+1}=========")
                    text += "${i + 1}번째 맛집  \n"

                    println("1. 위치 : ${elem.getElementsByTagName("SIGUN_NM").item(0).textContent}")
                    text += "1. 위치 : ${elem.getElementsByTagName("SIGUN_NM").item(0).textContent} \n"

                    println("2. 음식점 이름: ${elem.getElementsByTagName("RESTRT_NM").item(0).textContent}")
                    text += "2. 음식점 이름 : ${elem.getElementsByTagName("RESTRT_NM").item(0).textContent} \n"

                    println("3. 메뉴 : ${elem.getElementsByTagName("REPRSNT_FOOD_NM").item(0).textContent}")
                    text += "3. 메뉴 : ${elem.getElementsByTagName("REPRSNT_FOOD_NM").item(0).textContent} \n"

                    println("4. 전화번호 : ${elem.getElementsByTagName("TASTFDPLC_TELNO").item(0).textContent}")
                    text += "4. 전화번호 : ${elem.getElementsByTagName("TASTFDPLC_TELNO").item(0).textContent} \n"

                    println("5. 주소 : ${elem.getElementsByTagName("REFINE_LOTNO_ADDR").item(0).textContent}")
                    text += "5. 주소 : ${elem.getElementsByTagName("REFINE_LOTNO_ADDR").item(0).textContent} \n"

                }
            }
        } catch (e: Exception) {
            Log.d("TTT", "오픈API"+e.toString())
        }
    }
}