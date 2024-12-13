package meow.softer.yuyuan.data.local.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import meow.softer.yuyuan.YuyuanSetting
import java.io.InputStream
import java.io.OutputStream

object YuyuanSettingSerializer : Serializer<YuyuanSetting> {
    override val defaultValue: YuyuanSetting = YuyuanSetting.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): YuyuanSetting {
        try {
            return YuyuanSetting.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: YuyuanSetting, output: OutputStream) =
        t.writeTo(output)
}

val Context.yuyuanSettingDataStore: DataStore<YuyuanSetting> by dataStore(
    fileName = "settings.pb",
    serializer = YuyuanSettingSerializer
)