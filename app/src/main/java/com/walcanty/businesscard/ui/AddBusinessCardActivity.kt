package com.walcanty.businesscard.ui

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.graphics.ColorUtils
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.google.android.material.button.MaterialButton
import com.walcanty.businesscard.App
import com.walcanty.businesscard.R
import com.walcanty.businesscard.data.BusinessCard
import com.walcanty.businesscard.databinding.ActivityAddBusinessCardBinding

class AddBusinessCardActivity : AppCompatActivity() {

    private var myColor = "#FF018786"
    private val binding by lazy { ActivityAddBusinessCardBinding.inflate(layoutInflater) }

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as App).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        insertListener()
        getExtra()
    }

    private fun getExtra() {
        if (intent.hasExtra(TASK_ID)) {
            binding.tvTitle.text = "Редактировать"
            val cardId = intent.getIntExtra(TASK_ID, 0)

            mainViewModel.getCardById(cardId).let {
                binding.apply {
                    tilName.editText?.setText(it.name)
                    tilPhone.editText?.setText(it.phone)
                    tilEmail.editText?.setText(it.email)
                    tilCompany.editText?.setText(it.company)
                    btnColor.setBackgroundColor(Color.parseColor(it.background))
                    setButtonBackground(btnColor, Color.parseColor(it.background))

                }

                myColor = it.background
            }

            Log.e("TAG", "getExtra ${cardId}")
        }
    }

    private fun insertListener() {
        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnColor.setOnClickListener {
            ColorPickerDialog
                .Builder(this)                        // Pass Activity Instance
                .setTitle("Pick Theme")            // Default "Choose Color"
                .setColorShape(ColorShape.SQAURE)   // Default ColorShape.CIRCLE
                .setDefaultColor(myColor)     // Pass Default Color
                .setColorListener { color, colorHex ->
                    // Handle Color Selection
                    myColor = colorHex
                    val colorPickerBtn = binding.btnColor
                    setButtonBackground(colorPickerBtn, color)
                    Log.e("TAG", "$colorHex ")
                }
                .show()
        }

        binding.btnConfirm.setOnClickListener {


            if (TextUtils.isEmpty(binding.tilName.editText?.text) ||
                TextUtils.isEmpty(binding.tilPhone.editText?.text) ||
                TextUtils.isEmpty(binding.tilEmail.editText?.text) ||
                TextUtils.isEmpty(binding.tilCompany.editText?.text)
            ) {
                Toast.makeText(this, "Все поля должны быть заполнены!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val businessCard = BusinessCard(
                    name = binding.tilName.editText?.text.toString().trim(),
                    company = binding.tilCompany.editText?.text.toString().trim(),
                    phone = binding.tilPhone.editText?.text.toString().trim(),
                    email = binding.tilEmail.editText?.text.toString().trim(),
                    id = intent.getIntExtra(TASK_ID, 0),
                    background = myColor
                )
                mainViewModel.insert(businessCard)

                if (intent.hasExtra(TASK_ID)){
                    Toast.makeText(this, R.string.label_show_update, Toast.LENGTH_SHORT).show()
                }  else{
                    Toast.makeText(this, R.string.label_show_success, Toast.LENGTH_SHORT).show()
                }

                finish()
            }

        }

    }

    private fun setButtonBackground(btn: MaterialButton, color: Int) {

        val whiteContrast = ColorUtils.calculateContrast(Color.WHITE, color)
        val blackContrast = ColorUtils.calculateContrast(Color.BLACK, color)

        if (whiteContrast > blackContrast) btn.setTextColor(Color.WHITE) else btn.setTextColor(Color.BLACK)


        btn.backgroundTintList = ColorStateList.valueOf(color)
    }


    companion object {
        const val TASK_ID = "task_id"
    }

}