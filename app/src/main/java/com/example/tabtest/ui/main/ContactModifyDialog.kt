package com.example.tabtest.ui.main

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager
import com.example.tabtest.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.util.*
import java.util.jar.Attributes

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PhotoDialog.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactModifyDialog : DialogFragment() {

    lateinit var mAdapter: CustomAdapter
    lateinit var contactModel: ContactModel
    var OriginalName: String = ""
    var OriginalNumber: String = ""

    companion object {

        const val TAG = "SimpleDialog"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"

        fun newInstance(title: String, subTitle: String): ContactModifyDialog {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            val fragment = ContactModifyDialog()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

//        val root = inflater.inflate(R.layout.fragment_photo_dialog,container, false)
//        val mAdapter= PhotoPagerAdapter(requireContext(),PhotoArray)
//        val mViewPager : ViewPager = root.findViewById(R.id.photo_view_pager)
//        mViewPager.adapter = mAdapter


        return inflater.inflate(R.layout.fragment_contact_add_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        val SubmitButton: Button = view.findViewById(R.id.button_submit)
        val NameEditText: EditText = view.findViewById(R.id.edittext_name)
        val NumberEditText: EditText = view.findViewById(R.id.edittext_phone)

        NameEditText.setText(contactModel.name)
        NumberEditText.setText(contactModel.number)

        val originalName = contactModel.name
        val originalNumber = contactModel.number

        SubmitButton.setOnClickListener {
            val Name: String = NameEditText.text.toString()
            val Number: String = NumberEditText.text.toString()
//            var NewContact: ContactModel = ContactModel(null, Firebase.auth.currentUser?.uid ,Name, Number)
            contactModel.name = Name
            contactModel.number = Number
            val call = ContactApiObject.retrofitService.ModifyContact(contactModel._id, contactModel)
            call.enqueue(object: retrofit2.Callback<UpdateContact> {
                override fun onFailure(call: Call<UpdateContact>, t: Throwable) {
                    contactModel.name = originalName
                    contactModel.number = originalNumber
                }
                override fun onResponse(call: Call<UpdateContact>, response: retrofit2.Response<UpdateContact>) {
//                    NewContact._id = response.body()?.id
//                    println(NewContact._id)
                    if(response.isSuccessful){
                        println("수정 성공")
                        response.body()?.message?.let {
                            if (response.body()?.message == "contact updated") {
                                mAdapter.notifyDataSetChanged()
                            }
                            else{
                                contactModel.name = originalName
                                contactModel.number = originalNumber
                            }
                            }


                    }
                    else{
                        println(response.errorBody().toString())
                        println("수정 성공? 실패")
                        contactModel.name = originalName
                        contactModel.number = originalNumber
                    }
                }
            })

//            mAdapter.addItem(NewContact)
            dismiss()
        }



//        val mAdapter= PhotoPagerAdapter(requireContext(),PhotoArray) // make adapter
//        val mViewPager : ViewPager = view.findViewById(R.id.photo_view_pager) // make viewpager object
//        mViewPager.adapter = mAdapter // connect viewpager adapter
//        mViewPager.setCurrentItem(PhotoPosition) // set current page of view pager


    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupView(view: View) {
//        view.tvTitle.text = arguments?.getString(KEY_TITLE)
//        view.tvSubTitle.text = arguments?.getString(KEY_SUBTITLE)


    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val root = inflater.inflate(R.layout.fragment_photo_dialog,container, false)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onResume() {

        requireDialog().window?.setLayout(1000,1500) // Set dialog size
        super.onResume()

    }

}