import { Component, OnInit, AfterViewChecked} from '@angular/core';
import { Media } from '../../models/media';
import { MediaService } from '../../services/media/media.service';
import { AuthentificationService } from '../../services/authentification.service';

@Component({
  selector: 'app-medialist',
  templateUrl: './media-list.component.html',
  styleUrls: ['./media-list.component.css']
})
export class MediaListComponent implements OnInit, AfterViewChecked {
  medias: Media[] = [];

  constructor(private mediaService: MediaService) {}

  ngOnInit() {
    this.mediaService.getAllMedias().subscribe(medias => {
      this.medias = medias;
    });

  }


  ngAfterViewChecked() {
    this.modalImage();
  }

  modalImage() {
    // Get the modal
    const modal = $('#myModal');
    // Get the image and insert it inside the modal - use its "alt" text as a caption
    const img = $('#myImg');
    const modalImg = $('#img01');
    const captionText = $('#caption');
    img.click(function () {
      modal.show();
      modalImg.attr('src', $(this).attr('src'));
      captionText.html($(this).attr('alt'));
      $('.navbar').css('z-index', '0');
      $('#slide-out').css('z-index', '0');
    });

    // Get the <span> element that closes the modal
    const span = $('#close');

    // When the user clicks on <span> (x), close the modal
    span.click(function () {
      modal.hide();
      $('.navbar').css('z-index', '');
      $('#slide-out').css('z-index', '');
    });

  }

  mediaUrl(id: string) {
    return this.mediaService.buildUrlToMediaWithId(id);
  }
}
