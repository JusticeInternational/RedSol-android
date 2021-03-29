package com.cleteci.redsolidaria.ui.organization


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Post
import kotlinx.android.synthetic.main.fragment_organization_activities.*


class ActivitiesFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private val postList: java.util.ArrayList<Post> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
        initHardCodeData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_organization_activities, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpUI()
    }

    private fun setUpUI() {
        setupPostsList()
        if (BaseApp.prefs.is_provider_service) {
            btPublish.visibility = View.VISIBLE
            btPublish.setOnClickListener {
                Toast.makeText(context,"Mostra Publicar Post", Toast.LENGTH_LONG).show()
            }
        } else {
            btPublish.visibility = View.GONE
        }
    }

    private fun setupPostsList() {
        postsRecyclerView.layoutManager = LinearLayoutManager(context)
        postsRecyclerView.adapter = PostsAdapter(postList,  requireContext())
    }

    //Temporal Function to see UI Changes
    private fun initHardCodeData() {
        postList.add(Post("0","Santa Clara Valley Medical...","Hace 2 días",
            "Descripción para el primer post, Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique."))
        postList.add(Post("1","Santa Clara Valley Medical...","Hace 1 día",
            "Algo Nuevo para el 2do post, Lorem ipsum dolor sit amet. "))
        postList.add(Post("2","Santa Clara Valley Medical...","Ayer",
            " Noticia de ultima hora para 3er post, consectetur adipiscing elit. Ut varius justo at dui dictum tristique."))
        postList.add(Post("3","Santa Clara Valley Medical...","hoy",
            "Una history para recordar, Lorem ipsum dolor sit amet, consectetur adipiscing elit."))
        postList.add(Post("4","Santa Clara Valley Medical...","Hace 2 días",
            "Descripción para el primer post, Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique."))
        postList.add(Post("5","Santa Clara Valley Medical...","Hace 1 día",
            "Algo Nuevo para el 2do post, Lorem ipsum dolor sit amet. "))
        postList.add(Post("6","Santa Clara Valley Medical...","Ayer",
            " Noticia de ultima hora para 3er post, consectetur adipiscing elit. Ut varius justo at dui dictum tristique."))
        postList.add(Post("7","Santa Clara Valley Medical...","hoy",
            "Una history para recordar, Lorem ipsum dolor sit amet, consectetur adipiscing elit."))
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        @JvmStatic
        fun newInstance(sectionNumber: Int): ActivitiesFragment {
            return ActivitiesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}