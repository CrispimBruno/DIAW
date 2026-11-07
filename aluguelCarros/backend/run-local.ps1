$ErrorActionPreference = 'Stop'

if (-not $env:DB_URL) {
    $env:DB_URL = 'jdbc:postgresql://db.sjvceyxyrcyswbdpdwnd.supabase.co:5432/postgres?sslmode=require'
}
if (-not $env:DB_USER) { $env:DB_USER = 'postgres' }
if (-not $env:JWT_SECRET) {
    $env:JWT_SECRET = [Guid]::NewGuid().ToString('N') + [Guid]::NewGuid().ToString('N')
}

$senhaInformada = -not $env:DB_PASSWORD
try {
    if ($senhaInformada) {
        $senha = Read-Host 'Senha do banco PostgreSQL (Supabase)' -AsSecureString
        $ponteiro = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($senha)
        try {
            $env:DB_PASSWORD = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($ponteiro)
        } finally {
            [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($ponteiro)
            $senha.Dispose()
        }
        if (-not $env:DB_PASSWORD) { throw 'A senha do banco e obrigatoria.' }
    }
    Push-Location $PSScriptRoot
    try {
        & mvn.cmd spring-boot:run
        if ($LASTEXITCODE -ne 0) { throw 'O back-end nao iniciou. Consulte o erro acima.' }
    } finally {
        Pop-Location
    }
} finally {
    if ($senhaInformada) { Remove-Item Env:DB_PASSWORD -ErrorAction SilentlyContinue }
}
