package com.sample.insta.di.scope

import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.SOURCE)
annotation class ActivityScope


@Scope
@Retention(AnnotationRetention.SOURCE)
annotation class FragmentScope

@Scope
@Retention(AnnotationRetention.SOURCE)
annotation class ViewModelScope

@Scope
@Retention(AnnotationRetention.SOURCE)
annotation class ViewHolderScope