package com.cleteci.redsolidaria.ui.organizationProfile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Organization
import com.cleteci.redsolidaria.models.Post
import com.cleteci.redsolidaria.util.SharedPreferences.Companion.getStringObj
import kotlinx.android.synthetic.main.fragment_organization_activities.*


class ActivitiesFragment : Fragment() {

    private val postList: java.util.ArrayList<Post> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initHardCodeData()//TODO Should be change for getOrganizationActivities query
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_organization_activities, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpUI()
    }

    private fun setUpUI() {
        setupPostsList()
        if (BaseApp.sharedPreferences.isProviderService) {
            btPublish.visibility = View.VISIBLE
            btPublish.setOnClickListener {
                Toast.makeText(context, getString(R.string.in_build), Toast.LENGTH_LONG).show()
            }
        } else {
            btPublish.visibility = View.GONE
        }
    }

    private fun setupPostsList() {
        postsRecyclerView.layoutManager = LinearLayoutManager(context)
        postsRecyclerView.adapter = PostsAdapter(postList, requireContext())
    }

    //Temporal Function to see UI Changes
    private fun initHardCodeData() {
        val name = getStringObj(Organization.Attribute.NAME.name) ?: "Name Not Fount"
        postList.add(
            Post(
                "0", name, "Hace 2 días",
                "Descripción para el primer post, Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique."
            )
        )
        postList.add(
            Post(
                "1", name, "Hace 1 día",
                "Algo Nuevo para el 2do post, Lorem ipsum dolor sit amet. "
            )
        )
        postList.add(
            Post(
                "2", name, "Ayer",
                " Noticia de ultima hora para 3er post, consectetur adipiscing elit. Ut varius justo at dui dictum tristique."
            )
        )
        postList.add(
            Post(
                "3", name, "hoy",
                "Una history para recordar, Lorem ipsum dolor sit amet, consectetur adipiscing elit."
            )
        )
        postList.add(
            Post(
                "4", name, "Hace 2 días",
                "Descripción para el primer post, Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut varius justo at dui dictum tristique."
            )
        )
        postList.add(
            Post(
                "5", name, "Hace 1 día",
                "Algo Nuevo para el 2do post, Lorem ipsum dolor sit amet. "
            )
        )
        postList.add(
            Post(
                "6", name, "Ayer",
                " Noticia de ultima hora para 3er post, consectetur adipiscing elit. Ut varius justo at dui dictum tristique."
            )
        )
        postList.add(
            Post(
                "7", name, "hoy",
                "Una history para recordar, Lorem ipsum dolor sit amet, consectetur adipiscing elit."
            )
        )
    }
}