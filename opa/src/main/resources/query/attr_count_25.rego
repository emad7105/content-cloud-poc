package accountstates

default allow = false

allow {
  data.accountState.selectivity10 == token.payload.select_10
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
  data.accountState.attribute16 == token.payload.attribute16
  data.accountState.attribute17 == token.payload.attribute17
  data.accountState.attribute18 == token.payload.attribute18
  data.accountState.attribute19 == token.payload.attribute19
  data.accountState.attribute20 == token.payload.attribute20
  data.accountState.attribute21 == token.payload.attribute21
  data.accountState.attribute22 == token.payload.attribute22
  data.accountState.attribute23 == token.payload.attribute23
#  data.accountState.attribute24 == token.payload.attribute24
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}