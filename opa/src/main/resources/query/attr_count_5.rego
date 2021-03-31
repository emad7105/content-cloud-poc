package accountstates

default allow = false

allow {
  data.accountState.attribute0 == token.payload.attribute0
  data.accountState.attribute1 == token.payload.attribute1
  data.accountState.attribute2 == token.payload.attribute2
  data.accountState.attribute3 == token.payload.attribute3
  data.accountState.attribute4 == token.payload.attribute4
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
