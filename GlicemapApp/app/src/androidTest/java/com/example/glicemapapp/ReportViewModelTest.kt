package com.example.glicemapapp.ui.home

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.glicemapapp.data.Result
import com.example.glicemapapp.data.models.User
import com.example.glicemapapp.ui.base.getOrAwaitValue
import com.example.glicemapapp.ui.main.report.ReportViewModel
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ReportViewModelTest : TestCase() {

    private lateinit var  viewModel: ReportViewModel


    @Before
    public override fun setUp() {
        super.setUp()

        viewModel= ReportViewModel()
        viewModel.user = User(
            "39965187819",
            "Victor Padula",
            "vhpadula@usp.br",
            "seEuQuiserFalarComDeus2021#$",
            "29-07-2000",
            "183",
            "75",
            "90",
            "120"
        )
    }


    @Test
    fun testGetPDF(){
        val result = viewModel.getPDF().getOrAwaitValue {

        }
        assertEquals(Result.Success::class.simpleName, result::class.simpleName)
    }
}