package com.taskail.mixion.steemdiscussion

import `in`.uncod.android.bypass.Bypass
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import com.taskail.mixion.R
import com.taskail.mixion.data.SteemitDataSource
import com.taskail.mixion.data.models.ContentReply
import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.main.steemitRepository
import com.taskail.mixion.utils.*
import com.taskail.mixion.utils.html2md.HTML2Md
import com.taskail.mixion.utils.steemitutils.*

/**Created by ed on 10/6/17.
 */

internal const val openDiscussion = "DiscussionFromFeed"

internal const val loadDiscussionAuthor = "LoadDiscussionAuthor"

internal const val loadDiscussionPermlink = "LoadDiscussionPermlink"

fun openDiscussionIntent(context: Context, discussion: SteemDiscussion) : Intent{
    return Intent()
            .setClass(context, DiscussionDetailsActivity::class.java)
            .putExtra(openDiscussion, discussion)
}

fun loadDiscussionIntent(context: Context, author: String, permlink: String): Intent{
    return Intent()
            .setClass(context, DiscussionDetailsActivity::class.java)
            .putExtra(loadDiscussionAuthor, author)
            .putExtra(loadDiscussionPermlink, permlink)
}

class DiscussionDetailsActivity : AppCompatActivity(),
        DiscussionContract.Presenter {

    private lateinit var discussionsView: DiscussionContract.View

    override fun start() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discussion_details)

        discussionsView = supportFragmentManager
                .findFragmentById(R.id.discussion_details_fragment) as DiscussionContract.View

        discussionsView.presenter = this

        handleIntent(intent)
    }

    private fun handleIntent(@NonNull intent: Intent) {
        val extra = intent.extras[openDiscussion]
        if (extra != null) {
            val discussion: SteemDiscussion = extra as SteemDiscussion
            setDiscussion(discussion)
        } else {
            val author = intent.getStringExtra(loadDiscussionAuthor)
            val permlink = intent.getStringExtra(loadDiscussionPermlink)

            if (author != null && permlink != null) {
                loadDiscussion(author, permlink)
            }
        }
    }

    private fun loadDiscussion(author: String, permlink: String) {
        steemitRepository?.getDiscussion(author, permlink, object : SteemitDataSource.DiscussionLoadedCallBack {
            override fun onDataLoaded(discussion: SteemDiscussion) {
                setDiscussion(discussion)
            }

            override fun onLoadError(error: Throwable) {
                //TODO - add loading error
            }
        })
    }

    private fun setDiscussion(discussion: SteemDiscussion) {
        discussionsView.displayTitle(discussion.title)
        discussionsView.displayBtnInfo(discussion.netVotes.toString(),
                discussion.pendingPayoutValue.replace("SBD", ""),
                discussion.author,
                GetTimeAgo.getlongtoago(discussion.created))

        val newbody = HTML2Md.convert(discussion.body)
        discussionsView.displayMarkdownBody(newbody, getBypass())
        if (discussion.children > 0){
            loadComments(discussion.author, discussion.permlink)
        } else {
            discussionsView.noComments()
        }
    }

    private fun loadComments(author: String, permlink: String){
        steemitRepository?.remoteRepository?.getComments(author, permlink, object :
                SteemitDataSource.DataLoadedCallback<ContentReply> {
            override fun onDataLoaded(list: List<ContentReply>) {
                // Not needed, remove and fix this
            }

            override fun onDataLoaded(array: Array<ContentReply>) {
                discussionsView.displayComments(array)
            }

            override fun onLoadError(error: Throwable) {
            }

        })
    }

    private fun getBypass(): Bypass{
        val option = Bypass.Options()
                .setBlockQuoteLineColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .setBlockQuoteLineWidth(2) // dps
                .setBlockQuoteLineIndent(8) // dps
                .setPreImageLinebreakHeight(4) //dps
               .setBlockQuoteIndentSize(TypedValue.COMPLEX_UNIT_DIP, 2f)
                .setBlockQuoteTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        return Bypass(this, option)
    }
}