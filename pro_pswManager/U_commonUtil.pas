unit U_commonUtil;

interface

uses IniFiles,Windows,SysUtils;


  

implementation



function readIni(const fileName,aModule, aName,
  aDefault: String): String;
var
  iniFile: TIniFile;
begin
  iniFile := TIniFile.Create(fileName);
  try
    result := iniFile.ReadString(aModule, aName, aDefault);
  finally
    FreeAndNil(iniFile);
  end;
end;

procedure writeIni(const fileName,aModule, aName,
  aValue: String);
var
  iniFile: TIniFile;
begin
  iniFile := TIniFile.Create(fileName);
  try
   iniFile.WriteString(aModule, aName, aValue);
//    result := iniFile.(aModule, aName, aDefault);
  finally
    FreeAndNil(iniFile);
  end;
end;

function GetModulePath: string;
var
  sBuf: PChar;
  nTemp: integer;
begin
  GetMem(sBuf, MAX_PATH);
  GetModuleFileName(0, sBuf, MAX_PATH);
  nTemp := length(sBuf);
  while nTemp > 0 do
  begin
    if sBuf[nTemp] = '\' then
    begin
      break;
    end;
    dec(nTemp);
  end;
  result := Copy(sBuf, 1, nTemp);
  FreeMem(sBuf);
end;




end.
