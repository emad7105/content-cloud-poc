package accountstate
# Error 1: QueryDSLConverter:63 -> ADD => AND
# Error 2: QUERYDSL error: "org.hibernate.QueryException: could not resolve property: $01 of: be.heydari.contentcloud.accountstateservice.AccountStateAttribute [select count(entity) from be.heydari.contentcloud.accountstateservice.AccountState entity where entity.attributes.$01.name = ?1 and ( ( entity.attributes.$01.name = 'clearanceLevel'   AND  entity.attributes.$01.value = 'top-secret'  ) ) and ( ( entity.attributes.$01.name = 'clearanceLevel'   AND  entity.attributes.$01.value = 'top-secret'  ) )]"

default allow_partial = false

allow_partial {
  support_allow
  attr := data.accountState.attributes[_]
  attr.name == "clearanceLevel"
  attr.value == token.payload.clearanceLevel
}

default support_allow = false

support_allow {
    token.payload.broker == "broker0"
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}