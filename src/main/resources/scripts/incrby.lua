local key = KEYS[1];
local incrNum = ARGV[1];
local keyExist = redis.call("exists", key) == 1;
if keyExist then
    local value = redis.call("get", key);
    if incrNum ~= nil then value = value + incrNum else value = value + 1 end;
    redis.call("set", key, value);
    return value;
else
    local value = 1;
    if incrNum ~= nil then value = incrNum end;
    redis.call("set", key, value);
    return value;
end