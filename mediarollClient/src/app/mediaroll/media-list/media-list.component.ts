import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Media } from '../../models/media';
import { MediaService } from '../../services/media/media.service';

@Component({
  selector: 'app-medialist',
  templateUrl: './media-list.component.html',
  styleUrls: ['./media-list.component.css']
})
export class MediaListComponent implements OnInit {

  medias: Media[] = [];

  constructor(private mediaService: MediaService, public sanitizer: DomSanitizer) { }

  ngOnInit() {
    this.mediaService.getAllMedias().subscribe(medias => {
      console.log(medias);
      this.medias = medias;
    });
  }

  ngAfterViewChecked() {
    ($('.materialboxed') as any).materialbox();
  }

  mediaUrl(encodedMedia: string){
    return this.sanitizer.bypassSecurityTrustResourceUrl(encodedMedia);
  }

}
