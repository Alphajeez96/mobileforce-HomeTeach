package com.mobileforce.hometeach.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobileforce.hometeach.R
import com.mobileforce.hometeach.adapters.CircleTransform
import com.mobileforce.hometeach.data.model.TutorEntity
import com.mobileforce.hometeach.data.sources.remote.wrappers.TutorNetworkResponse
import com.mobileforce.hometeach.models.TutorModel
import com.squareup.picasso.Picasso
import java.lang.reflect.Method

@BindingAdapter("imagecircular")
fun ImageView.bindTutorImage(tutorImage: String) {
    tutorImage.let {
        Picasso.get().load(tutorImage).transform(CircleTransform())
            .placeholder(R.drawable.profile_image).error(
                R.drawable.profile_image
            ).into(this)
    }
}

/**
 * This function helps to toggle the visibility of a [View]. If the condition
 * is met, the [View] is made visible else it is hidden.
 */
inline fun <T : View> T.showIf(condition: (T) -> Boolean) {
    visibility = if (condition(this)) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

/**
 * This functions helps in transforming a [MutableLiveData] of type [T]
 * to a [LiveData] of type [T]
 */
fun <T> MutableLiveData<T>.asLiveData() = this as LiveData<T>


/**
 * Converts [TutorModel] to a [TutorEntity]
 */
fun TutorModel.toDbEntity() = TutorEntity(
    id, full_name, profile_pic, description, tutorSubject, hourly_rate, rating
)

/**
 * Converts [TutorEntity] to a [TutorModel]
 */
fun TutorEntity.toDomainModel() = TutorModel(
    id, full_name, profile_pic, description, tutorSubject, hourly_rate, rating
)

fun TutorNetworkResponse.toDomainModel(): TutorModel {
    val ratingModel = this.rating
    val userModel = this.user
    return TutorModel(
        id = userModel.id,
        full_name = userModel.full_name,
        profile_pic = this.profile_pic,
        description = this.description,
        tutorSubject = this.subjects,
        hourly_rate = this.hourly_rate,
        rating = ratingModel.rating
    )
}


fun pojo2Map(obj: Any): Map<String, Any> {
    val hashMap: MutableMap<String, Any> =
        HashMap()
    try {
        val c: Class<out Any> = obj.javaClass
        val m: Array<Method> = c.methods
        for (i in m.indices) {
            if (m[i].name.indexOf("get") === 0) {
                val name: String =
                    m[i].name.toLowerCase().substring(3, 4) + m[i].getName()
                        .substring(4)
                hashMap[name] = m[i].invoke(obj, arrayOfNulls<Any>(0))
            }
        }
    } catch (e: Throwable) {
        //log error
    }
    return hashMap
}
