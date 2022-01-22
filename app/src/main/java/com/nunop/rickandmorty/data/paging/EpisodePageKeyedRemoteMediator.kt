package com.nunop.rickandmorty.data.paging

import androidx.paging.ExperimentalPagingApi

@OptIn(ExperimentalPagingApi::class)
class EpisodePageKeyedRemoteMediator()
//    private val db: Database,
//    private val api: RickAndMortyAPI
//) : RemoteMediator<Int, ResultEpisode>() {
//    private val postDao: RedditPostDao = db.posts()
//    private val remoteKeyDao: SubredditRemoteKeyDao = db.remoteKeys()
//
//    override suspend fun initialize(): InitializeAction {
//        // Require that remote REFRESH is launched on initial load and succeeds before launching
//        // remote PREPEND / APPEND.
//        return InitializeAction.LAUNCH_INITIAL_REFRESH
//    }
//
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, ResultEpisode>
//    ): MediatorResult {
//        try {
//            // Get the closest item from PagingState that we want to load data around.
//            val loadKey = when (loadType) {
//                REFRESH -> null
//                PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
//                APPEND -> {
//                    // Query DB for SubredditRemoteKey for the subreddit.
//                    // SubredditRemoteKey is a wrapper object we use to keep track of page keys we
//                    // receive from the Reddit API to fetch the next or previous page.
//                    val remoteKey = db.withTransaction {
//                        remoteKeyDao.remoteKeyByPost(subredditName)
//                    }
//
//                    // We must explicitly check if the page key is null when appending, since the
//                    // Reddit API informs the end of the list by returning null for page key, but
//                    // passing a null key to Reddit API will fetch the initial page.
//                    if (remoteKey.nextPageKey == null) {
//                        return MediatorResult.Success(endOfPaginationReached = true)
//                    }
//
//                    remoteKey.nextPageKey
//                }
//            }
//
//            val data = api.getCharacters(page = 1).body()
//
//            val items = data.results
//
//            db.withTransaction {
//                if (loadType == REFRESH) {
//
//                }
//                items?.forEach {
//                    postDao.insertCharacter(it.toCharacter())
//                }
//            }
//
//            return MediatorResult.Success(endOfPaginationReached = items.isEmpty())
//        } catch (e: IOException) {
//            return MediatorResult.Error(e)
//        } catch (e: HttpException) {
//            return MediatorResult.Error(e)
//        }
//    }
//}
