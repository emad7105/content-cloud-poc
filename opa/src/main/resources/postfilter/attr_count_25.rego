package accountstates

default allow = false

allow {
  input.accountState.attribute0 == token.payload.attribute0
  input.accountState.attribute1 == token.payload.attribute1
  input.accountState.attribute2 == token.payload.attribute2
  input.accountState.attribute3 == token.payload.attribute3
  input.accountState.attribute4 == token.payload.attribute4
  input.accountState.attribute5 == token.payload.attribute5
  input.accountState.attribute6 == token.payload.attribute6
  input.accountState.attribute7 == token.payload.attribute7
  input.accountState.attribute8 == token.payload.attribute8
  input.accountState.attribute9 == token.payload.attribute9
  input.accountState.attribute10 == token.payload.attribute10
  input.accountState.attribute11 == token.payload.attribute11
  input.accountState.attribute12 == token.payload.attribute12
  input.accountState.attribute13 == token.payload.attribute13
  input.accountState.attribute14 == token.payload.attribute14
  input.accountState.attribute15 == token.payload.attribute15
  input.accountState.attribute16 == token.payload.attribute16
  input.accountState.attribute17 == token.payload.attribute17
  input.accountState.attribute18 == token.payload.attribute18
  input.accountState.attribute19 == token.payload.attribute19
  input.accountState.attribute20 == token.payload.attribute20
  input.accountState.attribute21 == token.payload.attribute21
  input.accountState.attribute22 == token.payload.attribute22
  input.accountState.attribute23 == token.payload.attribute23
  input.accountState.attribute24 == token.payload.attribute24
#  input.accountState.attribute25 == token.payload.attribute25
#  input.accountState.attribute26 == token.payload.attribute26
#  input.accountState.attribute27 == token.payload.attribute27
#  input.accountState.attribute28 == token.payload.attribute28
#  input.accountState.attribute29 == token.payload.attribute29
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}