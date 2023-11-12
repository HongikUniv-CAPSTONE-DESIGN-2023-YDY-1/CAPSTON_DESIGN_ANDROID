package com.example.capstonedesign.ui.fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.capstonedesign.R
import com.example.capstonedesign.databinding.FragmentMypageBinding
import com.example.capstonedesign.ui.activity.LogInActivity
import com.example.capstonedesign.utils.UserPreferences
import com.example.capstonedesign.ui.activity.ChangePasswordActivity
import com.example.capstonedesign.ui.activity.MyReviewActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class MypageFragment : Fragment() {

    private lateinit var binding: FragmentMypageBinding
    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreferences(requireContext())





        userPreferences.loginStatus.asLiveData().observe(viewLifecycleOwner) {
            val loginStatus = it ?: false
            if (loginStatus) {
                binding.btnProfile.visibility = View.VISIBLE
                binding.btnLogin.visibility = View.GONE
                binding.btnLogout.visibility = View.VISIBLE

            } else {
                binding.btnProfile.visibility = View.GONE
                binding.btnLogin.visibility = View.VISIBLE
                binding.btnLogout.visibility = View.GONE
                binding.btnChangePassword.setOnClickListener {
                    Toast.makeText(activity, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
                binding.btnGoToReview.setOnClickListener {
                    Toast.makeText(activity, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        userPreferences.userEmail.asLiveData().observe(viewLifecycleOwner) {
            val email = it ?: ""
            binding.btnProfile.text = "$email 님 안녕하세요!!"
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(activity, LogInActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(
                requireContext(),
                R.style.BottomSheetDialogTheme
            )
            val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
                R.layout.sign_bottom_sheet,
                view.findViewById(R.id.bottom_sheet) as LinearLayout?
            )

            bottomSheetView.findViewById<View>(R.id.btn_logout_confirm).setOnClickListener {
                lifecycleScope.launch {
                    userPreferences.clear()


                    restartApp(requireContext())
                }
                Toast.makeText(activity, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                bottomSheetDialog.dismiss()

            }
            bottomSheetView.findViewById<View>(R.id.btn_cancel).setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }
        binding.btnChangePassword.setOnClickListener {
            val intent = Intent(activity, ChangePasswordActivity::class.java)
            startActivity(intent)
        }
        binding.btnGoToReview.setOnClickListener {
            val intent = Intent(activity, MyReviewActivity::class.java)
            startActivity(intent)
        }

    }

    private fun restartApp(context: Context) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
        exitProcess(0)
    }


}