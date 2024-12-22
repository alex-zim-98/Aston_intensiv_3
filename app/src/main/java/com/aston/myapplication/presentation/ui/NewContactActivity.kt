package com.aston.myapplication.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.BundleCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.aston.myapplication.databinding.ActivityNewContactBinding
import com.aston.myapplication.domain.entity.Contact
import com.aston.myapplication.presentation.viewmodel.NewContactViewModel

class NewContactActivity : AppCompatActivity() {

    private var mode: String? = null
    private var contact: Contact? = null

    private val viewBinding by lazy {
        ActivityNewContactBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[NewContactViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        parseExtra()
        launchRightMode()

        observers()

        doOnTextChangeListeners()
    }

    private fun doOnTextChangeListeners() {
        viewBinding.etName.doOnTextChanged { _, _, _, _ ->
            viewModel.resetErrorName()
        }

        viewBinding.etLastname.doOnTextChanged { _, _, _, _ ->
            viewModel.resetErrorLastName()
        }

        viewBinding.etPhone.doOnTextChanged { _, _, _, _ ->
            viewModel.resetErrorPhone()
        }
    }

    private fun observers() {
        viewModel.errorInputName.observe(this) {
            viewBinding.tilName.error = if (it) "Enter correct name" else null
        }

        viewModel.errorInputLastName.observe(this) {
            viewBinding.tilLastname.error = if (it) "Enter correct lastname" else null
        }

        viewModel.errorInputPhone.observe(this) {
            viewBinding.tilPhone.error = if (it) "Enter correct phone" else null
        }

        viewModel.shouldClosed.observe(this) {
            finish()
        }

        viewModel.contactItem.observe(this) {
            viewBinding.etName.setText(it.name)
            viewBinding.etLastname.setText(it.lastName)
            viewBinding.etPhone.setText(it.phone)
        }
    }

    private fun parseExtra() {
        val mode = intent.extras?.getString(EXTRA_MODE)
        val contact = intent.extras?.let {
            BundleCompat.getParcelable(it, EXTRA_CONTACT, Contact::class.java)
        }

        if (mode == null) throw RuntimeException("Mode is absent!")

        if (mode != EDIT_MODE && mode != ADD_MODE) {
            throw RuntimeException("Mode: $mode doesn't exist!")
        }

        this.mode = mode

        if (mode == EDIT_MODE) {
            if (contact == null) throw RuntimeException("Params is absent!")
            this.contact = contact
        }
    }

    private fun launchRightMode() {
        when (mode) {
            ADD_MODE -> addMode()
            EDIT_MODE -> editMode()
            else -> throw RuntimeException("Mode is absent!")
        }
    }

    private fun editMode() {
        contact?.let {
            viewModel.getContactById(it.id)
            with(viewBinding) {
                btnAddChange.setOnClickListener {
                    val inputName = etName.text.toString()
                    val inputLastname = etLastname.text.toString()
                    val inputPhone = etPhone.text.toString()

                    viewModel.editExistContact(
                        inputName,
                        inputLastname,
                        inputPhone
                    )
                }
            }
        }
    }

    private fun addMode() {
        with(viewBinding) {
            btnAddChange.setOnClickListener {
                val name = etName.text.toString()
                val lastName = etLastname.text.toString()
                val phone = etPhone.text.toString()

                viewModel.addNewContact(name, lastName, phone)
            }
        }
    }

    companion object {
        private const val EXTRA_CONTACT = "contact"
        private const val EXTRA_MODE = "mode"

        private const val EDIT_MODE = "edit"
        private const val ADD_MODE = "add"

        fun newIntentAdd(context: Context) =
            Intent(context, NewContactActivity::class.java).apply {
                putExtra(EXTRA_MODE, ADD_MODE)
            }

        fun newIntentEdit(context: Context, contact: Contact) =
            Intent(context, NewContactActivity::class.java).apply {
                putExtra(EXTRA_MODE, EDIT_MODE)
                putExtra(EXTRA_CONTACT, contact)
            }
    }
}