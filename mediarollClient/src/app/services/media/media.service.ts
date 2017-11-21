import { Injectable } from '@angular/core';
import { HttpHeaders, HttpParams } from "@angular/common/http";
@Injectable()
export class MediaService {

  @ViewChild('selectedFile') selectedFileEl;

  constructor(private authenticationService: AuthenticationService) { }

  uploadFile() {
    let params = new HttpParams();

    let formData = new FormData();
    formData.append('upload', this.selectedFileEl.nativeElement.files[0])

    const options = {
      headers: new HttpHeaders().set('Authorization', this.loopBackAuth.accessTokenId),
      params: params,
      reportProgress: true,
      withCredentials: true,
    }

    this.http.post('http://localhost:3000/api/FileUploads/fileupload', formData, options)
      .subscribe(
      data => {
        console.log("Subscribe data", data);
      },
      (err: HttpErrorResponse) => {
        console.log(err.message, JSON.parse(err.error).error.message);
      }
      )
      .add(() => this.uploadBtn.nativeElement.disabled = false);//teardown

  }
