import {decodeTsRecord, asServingReqRes} from '../index';

fdescribe('Yoyoyo', () => {

  const data = `FZI/124VNZwAAAAAAAAAAAAAACwAAAAVAAAADwEKEgoDd3RmEgIIARoHZmRmZGZkZgAAAAMBqgYHCgNrZXkSABWSP9dyhmkMAAAAAAAAAAEAAAAsAAAAFQAAAA8BChIKA3d0ZhICCAEaB2ZkZmRmZGYAAAADAaoGBwoDa2V5EgAVkj/XcrLGFAAAAAAAAAACAAAALAAAABUAAAAPAQoSCgN3dGYSAggBGgdmZGZkZmRmAAAAAwGqBgcKA2tleRIAFZI/13LXOhwAAAAAAAAAAwAAACwAAAAVAAAADwEKEgoDd3RmEgIIARoHZmRmZGZkZgAAAAMBqgYHCgNrZXkSABWSP9dzCEUwAAAAAAAAAAQAAAAsAAAAFQAAAA8BChIKA3d0ZhICCAEaB2ZkZmRmZGYAAAADAaoGBwoDa2V5EgAVkj/Xcy2UwAAAAAAAAAAFAAAALAAAABUAAAAPAQoSCgN3dGYSAggBGgdmZGZkZmRmAAAAAwGqBgcKA2tleRIAFZI/13NTSLQAAAAAAAAABgAAACwAAAAVAAAADwEKEgoDd3RmEgIIARoHZmRmZGZkZgAAAAMBqgYHCgNrZXkSABWSP9dzfEQcAAAAAAAAAAcAAAAsAAAAFQAAAA8BChIKA3d0ZhICCAEaB2ZkZmRmZGYAAAADAaoGBwoDa2V5EgAVkj/Xc6Wu2AAAAAAAAAAIAAAALAAAABUAAAAPAQoSCgN3dGYSAggBGgdmZGZkZmRmAAAAAwGqBgcKA2tleRIAFZI/13PJOUgAAAAAAAAACQAAACwAAAAVAAAADwEKEgoDd3RmEgIIARoHZmRmZGZkZgAAAAMBqgYHCgNrZXkSAA==`
    

  it('decode', () => {
    const bytes = decodeBase64(data);

    const wtf = decodeTsRecord(bytes);

    const descrR = [];
    wtf.forEach(function(v) {
      const descrE = [];
      v.entries.forEach(function(x) {
        const reqRes = asServingReqRes(x.data);
        const reqS = JSON.stringify(reqRes.req.toJSON());
        const respS = JSON.stringify(reqRes.resp.toJSON());

        descrE.push(`\tEntry:${x.uid}`);
        descrE.push(`\t\tReq:${reqS}`);
        descrE.push(`\t\tResp:${respS}`);

      });
      const joined = descrE.join('\n');
      descrR.push(`Record:${v.ts}\n${joined}`);
    });
    const finalDescr = descrR.join('\n');
    console.log(finalDescr);
  });
});

function decodeBase64(input: string): Uint8Array {
  const raw = window.atob(input);

  const rawLength = raw.length;

  const array = new Uint8Array(new ArrayBuffer(rawLength));

  for (let i = 0; i < rawLength; i++) {
    array[i] = raw.charCodeAt(i);
  }
  return array;
}
