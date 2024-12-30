package com.example.movie_app_hiza.di

import com.example.movie_app_hiza.data.local.MovieDao
import com.example.movie_app_hiza.data.mappers.MovieMapper
import com.example.movie_app_hiza.data.remote.MovieApi
import com.example.movie_app_hiza.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieMapper(): MovieMapper {
        return MovieMapper()
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieDao: MovieDao,
        movieApi: MovieApi,
        movieMapper: MovieMapper
    ): MovieRepository {
        return MovieRepository(movieDao, movieApi, movieMapper)
    }
}
