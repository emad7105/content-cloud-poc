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
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}