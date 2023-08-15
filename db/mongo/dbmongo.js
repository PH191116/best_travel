db.createUser({
    user: 'master',
    pwd: 'debuggeandoideas',
    roles: [
        {
            role: 'readWrite',
            db: 'users',
        },
    ],
});
db.createCollection('app_users', { capped: false });

db.app_users.insert([
    {
        "username": "ragnar777",
        "dni": "VIKI771012HMCRG093",
        "enabled": true,
        "password": "$2a$10$ww/O.fwzF62qpZyV9BU2R.CM24jJ3wZsR6caZl01YnbOuGsQGKz3u",
        "role":
            {
                "granted_authorities": ["read"]
            }
    },
    {
        "username": "heisenberg",
        "dni": "BBMB771012HMCRR022",
        "enabled": true,
        "password": "$2a$10$uG1ApyOLGZLDbyP5SZDVM.fYdEPyFwDba78ACYNxueNEX0jS0hOLW",
        "role":
            {
                "granted_authorities": ["read"]
            }
    },
    {
        "username": "misterX",
        "dni": "GOTW771012HMRGR087",
        "enabled": true,
        "password": "$2a$10$B.oDrUHTFqdeEOZq.qf7RumrR11n8DM6QcKpg757mwi5WXZaeNBpe",
        "role":
            {
                "granted_authorities": ["read", "write"]
            }
    },
    {
        "username": "neverMore",
        "dni": "WALA771012HCRGR054",
        "enabled": true,
        "password": "$2a$10$fxqmTduXJysUbmadzi3Q2emOnb103YyD69.6dsPXW/KDLVxegIohq",
        "role":
            {
                "granted_authorities": ["write"]
            }
    }
]);