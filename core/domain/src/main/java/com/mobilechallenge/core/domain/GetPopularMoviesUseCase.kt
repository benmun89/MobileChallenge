package com.mobilechallenge.core.domain

import com.mobilechallenge.core.data.repository.MovieRemoteRepository
import com.mobilechallenge.core.model.data.IoDispatcher
import com.mobilechallenge.core.model.data.MovieResponseModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val movieRemoteRepository: MovieRemoteRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Int, MovieResponseModel>(dispatcher) {

    public override suspend fun execute(param: Int) = movieRemoteRepository.getMostPopularMovie(page = param)
}