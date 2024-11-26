package com.aptokenizer.pruebasnocompose

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.aptokenizer.tokenizer.TokenCollect
import com.aptokenizer.tokenizer.views.models.FieldState
import com.aptokenizer.tokenizer.views.models.OnFieldStateChangeListener
import com.aptokenizer.tokenizer.views.models.RegexRuleValidation
import com.aptokenizer.tokenizer.views.models.TextInputType
import com.aptokenizer.tokenizer.views.system.TREditText
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class CollectAndroidViewsActivity : AppCompatActivity() {
    private var buttonSaveAuthToken: MaterialButton? = null
    private var buttonClearAuthToken: MaterialButton? = null
    private var buttonTokenizeData: MaterialButton? = null
    private var buttonShowPin: MaterialButton? = null
    private var editTextAuthToken: TextInputEditText? = null
    private var cardDigitsEditText: TREditText? = null
    private var pinDigitsEditText: TREditText? = null
    private var cardTokenText: TextView? = null
    private var pinTokenText: TextView? = null
    private var tvErrors: TextView? = null
    private var cardToken: String? = null
    private var pinToken: String? = null
    private var panLength: Int? = null
    private var pinLength: Int? = null

    private var hide = true

    private val tokenCollect = TokenCollect()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_collect_android_views)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        initListeners()
    }

    private fun initViews() {
        editTextAuthToken = findViewById(R.id.text_authorization_token)
        buttonSaveAuthToken = findViewById(R.id.button_save_auth_token)
        buttonClearAuthToken = findViewById(R.id.button_clear_auth_token)
        buttonTokenizeData = findViewById(R.id.button_tokenize_data)
        buttonShowPin = findViewById(R.id.button_show_pin)
        cardDigitsEditText = findViewById(R.id.text_number_card_layout)
        pinDigitsEditText = findViewById(R.id.text_number_pin_layout)
        cardTokenText = findViewById(R.id.card_token_text)
        pinTokenText = findViewById(R.id.pin_token_text)
        tvErrors = findViewById(R.id.errors)

        cardDigitsEditText?.setTokenCollect(tokenCollect)
        pinDigitsEditText?.setTokenCollect(tokenCollect)

        configureEditFields()
    }

    private fun configureEditFields() {
        pinDigitsEditText?.setRegexPattern(
            listOf(
                RegexRuleValidation(
                    pattern = "^(?!.*(0123|1234|2345|3456|4567|5678|6789|7890|3210|4321|5432|6543|7654|8765|9876|0987)).*\$",
                    errorMessage = "The pin is not valid, it must not contain sequential numbers"
                ),
                RegexRuleValidation(
                    pattern = "^(?!.*(\\d)\\1{3}).*\$",
                    errorMessage = "The pin is not valid, it must not contain repeated numbers"
                ),
                RegexRuleValidation(
                    pattern = "^(\\d{4})?\$",
                    errorMessage = "The pin is not valid, it must have exactly 4 digits"
                )
            )
        )
        pinDigitsEditText?.setError("The pin is not valid.")
        pinDigitsEditText?.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD or InputType.TYPE_CLASS_NUMBER)

        cardDigitsEditText?.setTextInputType(TextInputType.CARD_NUMBER)
        cardDigitsEditText?.setRegexPattern(
            listOf(
                RegexRuleValidation(
                    pattern = "(\\d{16})",
                    errorMessage = "The pan is not valid, it must have exactly 16 digits"
                )
            )
        )
        cardDigitsEditText?.setMaxLength(16)
        cardDigitsEditText?.setOnFieldStateChangeListener(object : OnFieldStateChangeListener {
            override fun onStateChange(state: FieldState) {
                panLength = state.contentLength
                updateTokenizeButtonState()
            }
        })

        pinDigitsEditText?.setOnFieldStateChangeListener(object : OnFieldStateChangeListener {
            override fun onStateChange(state: FieldState) {
                pinLength = state.contentLength
                updateTokenizeButtonState()

                var errors = String()
                state.regexRuleValidationResult.forEach { validationPin ->
                    if (validationPin.isValid.not()) {
                        validationPin.errorMessage?.let {
                            errors = errors.plus(it+"\n")
                        }
                    }
                }
                tvErrors?.text = errors
            }
        })
    }

    private fun updateTokenizeButtonState() {
        buttonTokenizeData?.isEnabled =
            (panLength == 16 && pinLength == 4 && editTextAuthToken?.text?.isNotEmpty() == true)
    }

    private fun initListeners() {
        buttonTokenizeData?.isEnabled = false
        cardDigitsEditText?.isEnabled = false
        pinDigitsEditText?.isEnabled = false

        buttonClearAuthToken?.setOnClickListener {
            editTextAuthToken?.setText(String())
            editTextAuthToken?.isEnabled = true
            buttonSaveAuthToken?.isVisible = true
            buttonClearAuthToken?.isVisible = false
            cardDigitsEditText?.isEnabled = false
            pinDigitsEditText?.isEnabled = false
            updateTokenizeButtonState()
        }

        buttonSaveAuthToken?.setOnClickListener {
            if (editTextAuthToken?.text?.isNotEmpty() == true) {
                editTextAuthToken?.text?.toString()?.trim()
                    ?.let { token ->
                        tokenCollect.setAccessToken(token)
                    }
                showMessage(getString(R.string.authorization_token_saved))

                editTextAuthToken?.isEnabled = false
                buttonClearAuthToken?.isVisible = true
                buttonSaveAuthToken?.visibility = View.INVISIBLE
                cardDigitsEditText?.isEnabled = true
                pinDigitsEditText?.isEnabled = true
                updateTokenizeButtonState()
            } else {
                showMessage(getString(R.string.field_not_empty))
            }
        }

        buttonShowPin?.setOnClickListener {
            if (hide) {
                hide = false
                pinDigitsEditText?.setInputType(InputType.TYPE_CLASS_NUMBER)
            } else {
                hide = true
                pinDigitsEditText?.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD or InputType.TYPE_CLASS_NUMBER)
            }
            pinLength?.let {
                pinDigitsEditText?.setSelection(it)
            }

            // Focus
            pinDigitsEditText?.isEnabled = true
            pinDigitsEditText?.requestFocus()
            pinDigitsEditText?.post{
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY)
            }
        }

        buttonTokenizeData?.setOnClickListener {
            buttonTokenizeData?.text = getString(R.string.loading)
            buttonTokenizeData?.isEnabled = false
            tokenCollect.collect { result ->
                lifecycleScope.launch {
                    updateTokenizeButtonState()
                    buttonTokenizeData?.text = getString(R.string.tokenize_data_button)
                    when (result) {
                        is TokenCollect.TRResult.Success -> {
                            cardToken = result.listTokenizedData["number"] ?: ""
                            pinToken = result.listTokenizedData["pin"] ?: ""

                            if (cardToken?.isNotEmpty() == true) {
                                cardTokenText?.text = getString(R.string.text_view_pan_token, cardToken)
                            }
                            if (pinToken?.isNotEmpty() == true) {
                                pinTokenText?.text = getString(R.string.text_view_pin_token, pinToken)
                            }
                        }

                        is TokenCollect.TRResult.Error -> {
                            var errorMessage = result.error
                            if (result.message.isNotEmpty()) errorMessage =
                                errorMessage.plus(" - ${result.message}")
                            showMessage(errorMessage)
                        }

                        is TokenCollect.TRResult.NetworkError -> {
                            showMessage(getString(R.string.network_error))
                        }

                        is TokenCollect.TRResult.InvalidToken -> {
                            showMessage(getString(R.string.invalid_token))
                        }

                        is TokenCollect.TRResult.ValueNoValid -> {
                            showMessage(result.errorMessage)
                        }
                    }
                    buttonTokenizeData?.isEnabled = true
                }
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
