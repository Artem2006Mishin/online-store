$content = Get-Content 'F:\vs_code\online-store\frontend\src\layout\Layout.jsx' -Raw

$old = @"
`tlet navData = [
`t`t{ to: '/', text: 'РќРћР’РћРЎРўР’РћР™РўР’ РђРљРљРђРЈРќРў' },
`t];
`tif (status === 'success') {
`t`t navData = [
`t`t`t{to: '/', text: 'РќРћР’РћРЎРўРљРђРўРђР›РћР“'},
`t`t`t{to: '/profile', text: 'РџР РћР¤Р›Р¬'}
`t`t];
`t}
"@

$new = @"
`tlet navData;
`tif (status === 'success') {
`t`t navData = [
`t`t`t{ to: '/', text: 'НОВОСТИ' },
`t`t`t{ to: '/categories', text: 'КАТАЛОГ' },
`t`t`t{ to: '/profile', text: 'ПРОФИЛЬ' },
`t`t];
`t} else {
`t`t navData = [
`t`t`t{ to: '/', text: 'НОВОСТИ' },
`t`t`t{ to: '/auth', text: 'ВОЙТИ В АККАУНТ' },
`t`t];
`t}
"@

$content -replace [regex]::Escape($old), $new | Set-Content 'F:\vs_code\online-store\frontend\src\layout\Layout.jsx'