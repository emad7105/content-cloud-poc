package accountstates

default allow = false

allow {
  data.accountState.attribute0 == token.payload.attribute0
  data.accountState.attribute1 == token.payload.attribute1
  data.accountState.attribute2 == token.payload.attribute2
  data.accountState.attribute3 == token.payload.attribute3
  data.accountState.attribute4 == token.payload.attribute4
  data.accountState.attribute5 == token.payload.attribute5
  data.accountState.attribute6 == token.payload.attribute6
  data.accountState.attribute7 == token.payload.attribute7
  data.accountState.attribute8 == token.payload.attribute8
  data.accountState.attribute9 == token.payload.attribute9
  data.accountState.attribute10 == token.payload.attribute10
  data.accountState.attribute11 == token.payload.attribute11
  data.accountState.attribute12 == token.payload.attribute12
  data.accountState.attribute13 == token.payload.attribute13
  data.accountState.attribute14 == token.payload.attribute14
  data.accountState.attribute15 == token.payload.attribute15
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}