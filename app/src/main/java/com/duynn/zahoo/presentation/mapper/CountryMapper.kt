package com.duynn.zahoo.presentation.mapper

import com.duynn.zahoo.data.model.CountryData
import com.duynn.zahoo.domain.entity.Country

/**
 * Created by duynn100198 on 10/6/21.
 */
class CountryMapper : BaseMapper<CountryData, Country>() {
    override fun map(data: CountryData): Country {
        return Country(
            name = data.name,
            code = data.code,
            dialCode = data.dialCode
        )
    }
}
