$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path $PSScriptRoot -Parent
$toolsPath = Join-Path $projectRoot '.tools'
if (Test-Path (Join-Path $toolsPath 'apache-maven-3.9.9/bin/mvn.cmd')) { exit 0 }
New-Item -ItemType Directory -Force -Path $toolsPath | Out-Null
$zipPath = Join-Path $toolsPath 'maven.zip'
$url = 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip'
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
Write-Host 'Downloading and verifying Maven 3.9.9...'
Invoke-WebRequest -UseBasicParsing -Uri $url -OutFile $zipPath
$checksum = (Invoke-WebRequest -UseBasicParsing -Uri ($url + '.sha512')).Content
if ($checksum -is [byte[]]) { $checksum = [Text.Encoding]::UTF8.GetString($checksum) }
$sha = [Security.Cryptography.SHA512]::Create()
$stream = [IO.File]::OpenRead($zipPath)
try { $actual = [BitConverter]::ToString($sha.ComputeHash($stream)).Replace('-', '') }
finally { $stream.Dispose(); $sha.Dispose() }
if ($actual -ine ($checksum.Trim() -split '\s+')[0]) { throw 'Maven checksum mismatch.' }
Add-Type -AssemblyName System.IO.Compression.FileSystem
[IO.Compression.ZipFile]::ExtractToDirectory($zipPath, $toolsPath)
