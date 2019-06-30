package com.example.base64_encode_encode_decode

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun change(view:View){
        val rg:RadioGroup = findViewById(R.id.radioGroup)

        if(editText.text.toString() == "") {
            Deformed.text = getString(R.string.False)
            return
        }
        val ck:String  = findViewById<RadioButton>(rg.getCheckedRadioButtonId()).getText().toString()
        if(ck == getString(R.string.encode)){
            encoder()
        }else if(ck == getString(R.string.decode)){
            decoder()
        }
    }

    //Encode START
    private fun encoder(){

        val text: List<Char> = editText.text.toString().toList()
        val ans : MutableList<Int> = mutableListOf()

        text.forEach{
            val tmp:List<Int> = charToBinary(it)
            ans.addAll(tmp)
        }
        while((ans.size)%6 != 0){
            ans.add(0)
        }

        val ans2 :MutableList<Int> = hexToInt(ans)

        Deformed.text = intToChar(ans2)
    }

    private   fun charToBinary(Base:Char):List<Int>{
        var number = Base.toInt()
        val binary : MutableList<Int> = mutableListOf()
        for(i in 0..7){
            binary.add(0,number%2)
            number /= 2
        }
        return binary
    }

    private fun hexToInt(Base:MutableList<Int>):MutableList<Int>{
        val moji : MutableList<Int> = mutableListOf()
        for(i in 0..((Base.size)/6)-1){
            moji.add(0)
            var cross = 1
            val tmp : List<Int> = Base.take(6)
            for(k in 1..6)Base.removeAt(0) //先頭六文字削除
            for(j in 5 downTo 0){
                moji[i] += tmp[j]*cross
                cross *= 2
            }
        }
        return moji
    }

    private fun intToChar(Base:MutableList<Int>):String{
        val conversion: List<Char> = listOf('A','B','C','D','E','F','G','H','I','J','K',
            'L','M','N','O','P','Q','R','S','T','U','V',
            'W','X','Y','Z','a','b','c','d','e','f','g',
            'h','i','j','k','l','m','n','o','p','q','r',
            's','t','u','v','w','x','y','z','0','1','2',
            '3','4','5','6','7','8','9','+','/')



        var ans = ""
        Base.forEach{ans += conversion[it]}
        while((ans.length)%4 != 0){
            ans += '='
        }
        return ans
    }
    //Encode END
    
    //Decode START
    private fun decoder(){
        val base :List<Char> = editText.text.toList().dropLastWhile{it == '='} /*[drop last]で'='削除 */
        val ans:MutableList<Int> = mutableListOf()
        if(base.isEmpty()){
            Deformed.text = getString(R.string.None_String) /*文字の有無の確認 */
            return
        }

        if(charToInt(base) == null){
            Deformed.text = getString(R.string.Not_BASE64) /*BASE64に対応した文字の有無の確認 */
            return
        }

        val baseToInt :List<Int> = charToInt(base)!!
        for(i in baseToInt){
            val tmp:MutableList<Int> = intToBinary(i)
            ans.addAll(tmp)
        }

        var word = ""
        for(i in 1..((ans.size)/8)){
            val baseToChar:List<Int> = ans.take(8)
            for(c in 1..8)ans.removeAt(0)
            word += binaryToChar(baseToChar)
        }

        Deformed.text = word //出力場所//

    }

    private fun charToInt(char:List<Char>):List<Int>?{
        val conversion: List<Char> = listOf('A','B','C','D','E','F','G','H','I','J','K',
            'L','M','N','O','P','Q','R','S','T','U','V',
            'W','X','Y','Z','a','b','c','d','e','f','g',
            'h','i','j','k','l','m','n','o','p','q','r',
            's','t','u','v','w','x','y','z','0','1','2',
            '3','4','5','6','7','8','9','+','/')

        val ans:MutableList<Int> = mutableListOf()
        var c = 0
        char.forEach{
            if(!conversion.contains(it)){
                return null
            }
            ans.add(conversion.indexOf(it))
            c++
        }
        return ans.toList()
    }

    private fun intToBinary(int:Int):MutableList<Int>{
        val ans:MutableList<Int> = mutableListOf()
        var tmp = int
        for(i in 1..6){
            ans.add(0,tmp%2)
            tmp /= 2
        }
        return ans
    }

    private fun binaryToChar(binary:List<Int>):Char{
        var char = 0
        var cross = 1
        for(i in 7 downTo 0){
            char += binary[i]*cross
            cross *= 2
        }
        return char.toChar()
    }
}
